package com.coretex.core.business.services.catalog.product;


import com.coretex.core.business.repositories.catalog.product.ProductDao;
import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.image.ProductImageService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.services.search.SearchService;
import com.coretex.core.business.utils.CatalogServiceHelper;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.ProductList;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.ImageContentFile;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service("productService")
public class ProductServiceImpl extends SalesManagerEntityServiceImpl<ProductItem> implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

	ProductDao productDao;

	@Resource
	CategoryService categoryService;

	@Resource
	ProductRelationshipService productRelationshipService;

	@Resource
	SearchService searchService;

	@Resource
	ProductImageService productImageService;

	public ProductServiceImpl(ProductDao productDao) {
		super(productDao);
		this.productDao = productDao;
	}


	@Override
	public List<ProductItem> getProducts(List<UUID> categoryIds) {

		@SuppressWarnings({"unchecked", "rawtypes"})
		Set ids = new HashSet(categoryIds);
		return productDao.getProductsListByCategories(ids);

	}

	@Override
	public List<ProductItem> getProducts(List<UUID> categoryIds, LocaleItem language) {

		@SuppressWarnings({"unchecked", "rawtypes"})
		Set<Long> ids = new HashSet(categoryIds);
		return productDao.getProductsListByCategories(ids, language);

	}


	@Override
	public ProductItem getBySeUrl(MerchantStoreItem store, String seUrl, Locale locale) {
		return productDao.getByFriendlyUrl(store, seUrl, locale);
	}

	@Override
	public ProductItem getProductForLocale(UUID productId, LocaleItem language, Locale locale)
			 {
		ProductItem product = productDao.getProductForLocale(productId, language, locale);
		if (product == null) {
			return null;
		}

		CatalogServiceHelper.setToAvailability(product, locale);
		return product;
	}

	@Override
	public List<ProductItem> getProductsForLocale(CategoryItem category,
												  LocaleItem language, Locale locale)  {

		if (category == null) {
			throw new RuntimeException("The category is null");
		}

		//Get the category list
		StringBuilder lineage = new StringBuilder().append(category.getLineage()).append(category.getUuid()).append("/");
		List<CategoryItem> categories = categoryService.getListByLineage(category.getMerchantStore(), lineage.toString());
		Set<UUID> categoryIds = new HashSet<>();
		for (CategoryItem c : categories) {

			categoryIds.add(c.getUuid());

		}

		categoryIds.add(category.getUuid());

		//Get products
		List<ProductItem> products = productDao.getProductsForLocale(category.getMerchantStore(), categoryIds, language, locale);

		//Filter availability

		return products;
	}

	@Override
	public ProductList listByStore(MerchantStoreItem store,
								   LocaleItem language, ProductCriteria criteria) {

		return productDao.listByStore(store, language, criteria);
	}

	@Override
	public List<ProductItem> listByStore(MerchantStoreItem store) {

		return productDao.listByStore(store);
	}

	@Override
	public ProductItem getByCode(String productCode) {
		return productDao.getByCode(productCode);
	}


	@Override
	public void delete(ProductItem product) {
		LOGGER.debug("Deleting product");
		Validate.notNull(product, "ProductItem cannot be null");
		Validate.notNull(product.getMerchantStore(), "MerchantStoreItem cannot be null in product");
		product = this.getByUUID(product.getUuid());//Prevents detached entity error
		product.setCategories(null);

		Set<ProductImageItem> images = product.getImages();

		for (ProductImageItem image : images) {
			productImageService.removeProductImage(image);
		}

		product.setImages(null);

		//related - featured
		List<ProductRelationshipItem> relationships = productRelationshipService.listByProduct(product);
		for (ProductRelationshipItem relationship : relationships) {
			productRelationshipService.delete(relationship);
		}

		super.delete(product);
		searchService.deleteIndex(product.getMerchantStore(), product);

	}

	@Override
	public void create(ProductItem product) {
		this.saveOrUpdate(product);
		searchService.index(product.getMerchantStore(), product);
	}

	@Override
	public void update(ProductItem product) {
		this.saveOrUpdate(product);
		searchService.index(product.getMerchantStore(), product);
	}


	private void saveOrUpdate(ProductItem product) {
		LOGGER.debug("Save or update product ");
		Validate.notNull(product, "product cannot be null");
		Validate.notNull(product.getAvailabilities(), "product must have at least one availability");
		Validate.notEmpty(product.getAvailabilities(), "product must have at least one availability");


		//List of original images
		Set<ProductImageItem> originalProductImages = null;

		if (product.getUuid() != null) {
			originalProductImages = product.getImages();
		}


		super.save(product);

		/**
		 * Image creation needs extra service to save the file in the CMS
		 */
		List<UUID> newImageIds = new ArrayList<>();
		Set<ProductImageItem> images = product.getImages();

		try {

			if (images != null && images.size() > 0) {
				for (ProductImageItem image : images) {
					if ((image.getUuid() == null)) {
						image.setProduct(product);

//						InputStream inputStream = image.getImage();
						ImageContentFile cmsContentImage = new ImageContentFile();
						cmsContentImage.setFileName(image.getProductImage());
//						cmsContentImage.setFile(inputStream);
						cmsContentImage.setFileContentType(FileContentType.PRODUCT);

//						productImageService.addProductImage(product, image, cmsContentImage);
						newImageIds.add(image.getUuid());
					} else {
						productImageService.save(image);
						newImageIds.add(image.getUuid());
					}
				}
			}

			//cleanup old images
			if (originalProductImages != null) {
				for (ProductImageItem image : originalProductImages) {
					if (!newImageIds.contains(image.getUuid())) {
						productImageService.delete(image);
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("Cannot save images " + e.getMessage());
		}


	}


}
