package com.coretex.shop.store.controller.product.facade;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.services.catalog.product.review.ProductReviewService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.shop.model.catalog.manufacturer.PersistableManufacturer;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;
import com.coretex.shop.model.catalog.product.PersistableProduct;
import com.coretex.shop.model.catalog.product.PersistableProductReview;
import com.coretex.shop.model.catalog.product.ProductPriceEntity;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.catalog.product.ReadableProductList;
import com.coretex.shop.model.catalog.product.ReadableProductReview;
import com.coretex.shop.populator.catalog.PersistableProductPopulator;
import com.coretex.shop.populator.catalog.PersistableProductReviewPopulator;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.populator.catalog.ReadableProductReviewPopulator;
import com.coretex.shop.populator.manufacturer.PersistableManufacturerPopulator;
import com.coretex.shop.populator.manufacturer.ReadableManufacturerPopulator;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.ImageFilePath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("productFacade")
public class ProductFacadeImpl implements ProductFacade {

	@Resource
	private CategoryService categoryService;

	@Resource
	private ManufacturerService manufacturerService;

	@Resource
	private LanguageService languageService;

	@Resource
	private ProductService productService;

	@Resource
	private PricingService pricingService;

	@Resource
	private CustomerService customerService;

	@Resource
	private ProductReviewService productReviewService;

	@Resource
	private ProductRelationshipService productRelationshipService;

	@Resource
	private PersistableProductPopulator persistableProductPopulator;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Override
	public PersistableProduct saveProduct(MerchantStoreItem store, PersistableProduct product,
										  LanguageItem language) throws Exception {


		com.coretex.shop.model.catalog.manufacturer.Manufacturer manufacturer =
				product.getManufacturer();

		if (manufacturer == null
				|| (manufacturer.getUuid() == null)
				&& StringUtils.isBlank(manufacturer.getCode())) {

			// get default manufacturer
			ManufacturerItem defaultManufacturer = manufacturerService.getByCode(store, "DEFAULT");

			if (defaultManufacturer != null) {

				com.coretex.shop.model.catalog.manufacturer.Manufacturer m =
						new com.coretex.shop.model.catalog.manufacturer.Manufacturer();
				m.setUuid(defaultManufacturer.getUuid());
				m.setCode(defaultManufacturer.getCode());
				product.setManufacturer(m);

			}

		}

		ProductItem target = null;
		if (product.getUuid() != null) {
			target = productService.getById(product.getUuid());
		} else {
			target = new ProductItem();
		}


		persistableProductPopulator.populate(product, target, store, language);

		productService.create(target);

		product.setUuid(target.getUuid());

		return product;


	}

	@Override
	public ReadableProduct getProduct(MerchantStoreItem store, UUID id, LanguageItem language)
			throws Exception {

		ProductItem product = productService.getById(id);

		if (product == null) {
			return null;
		}

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, store, language);

		return readableProduct;
	}

	@Override
	public ReadableProduct getProduct(MerchantStoreItem store, String sku, LanguageItem language)
			throws Exception {

		ProductItem product = productService.getByCode(sku);

		if (product == null) {
			return null;
		}

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, store, language);

		return readableProduct;
	}

	@Override
	public ReadableProduct updateProductPrice(ReadableProduct product, ProductPriceEntity price,
											  LanguageItem language) throws Exception {


		ProductItem persistable = productService.getById(product.getUuid());

		if (persistable == null) {
			throw new Exception("product is null for id " + product.getUuid());
		}

		java.util.Set<ProductAvailabilityItem> availabilities = persistable.getAvailabilities();
		for (ProductAvailabilityItem availability : availabilities) {
			ProductPriceItem productPrice = availability.getPrices().stream().filter(ProductPriceItem::getDefaultPrice).findAny().get();
			productPrice.setProductPriceAmount(price.getOriginalPrice());
			if (price.isDiscounted()) {
				productPrice.setProductPriceSpecialAmount(price.getDiscountedPrice());
				if (!StringUtils.isBlank(price.getDiscountStartDate())) {
					Date startDate = DateUtil.getDate(price.getDiscountStartDate());
					productPrice.setProductPriceSpecialStartDate(startDate);
				}
				if (!StringUtils.isBlank(price.getDiscountEndDate())) {
					Date endDate = DateUtil.getDate(price.getDiscountEndDate());
					productPrice.setProductPriceSpecialEndDate(endDate);
				}
			}

		}

		productService.update(persistable);

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(persistable, readableProduct, persistable.getMerchantStore(), language);

		return readableProduct;
	}

	@Override
	public ReadableProduct updateProductQuantity(ReadableProduct product, int quantity,
												 LanguageItem language) throws Exception {
		ProductItem persistable = productService.getById(product.getUuid());

		if (persistable == null) {
			throw new Exception("product is null for id " + product.getUuid());
		}

		java.util.Set<ProductAvailabilityItem> availabilities = persistable.getAvailabilities();
		for (ProductAvailabilityItem availability : availabilities) {
			availability.setProductQuantity(quantity);
		}

		productService.update(persistable);

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(persistable, readableProduct, persistable.getMerchantStore(), language);

		return readableProduct;
	}

	@Override
	public void deleteProduct(ProductItem product) throws Exception {
		productService.delete(product);

	}

	@Override
	public ReadableProductList getProductListsByCriterias(MerchantStoreItem store, LanguageItem language,
														  ProductCriteria criterias) throws Exception {


		Validate.notNull(criterias, "ProductCriteria must be set for this product");

		if (CollectionUtils.isNotEmpty(criterias.getCategoryIds())) {


			if (criterias.getCategoryIds().size() == 1) {

				CategoryItem category =
						categoryService.getById(criterias.getCategoryIds().get(0));

				if (category != null) {
					String lineage = new StringBuilder().append(category.getLineage())
							.append(category.getUuid()).append("/").toString();

					List<CategoryItem> categories =
							categoryService.getListByLineage(store, lineage);

					List<UUID> ids = new ArrayList<>();
					if (categories != null && categories.size() > 0) {
						for (CategoryItem c : categories) {
							ids.add(c.getUuid());
						}
					}
					ids.add(category.getUuid());
					criterias.setCategoryIds(ids);
				}
			}


		}

		com.coretex.core.model.catalog.product.ProductList products =
				productService.listByStore(store, language, criterias);


		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);


		ReadableProductList productList = new ReadableProductList();
		for (ProductItem product : products.getProducts()) {

			// create new proxy product
			ReadableProduct readProduct =
					populator.populate(product, new ReadableProduct(), store, language);
			productList.getProducts().add(readProduct);

		}

		productList.setTotalCount(products.getTotalCount());


		return productList;
	}

	@Override
	public ReadableProduct addProductToCategory(CategoryItem category, ProductItem product, LanguageItem language)
			throws Exception {

		Validate.notNull(category, "CategoryItem cannot be null");
		Validate.notNull(product, "ProductItem cannot be null");

		product.getCategories().add(category);

		productService.update(product);

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, product.getMerchantStore(), language);

		return readableProduct;


	}

	@Override
	public ReadableProduct removeProductFromCategory(CategoryItem category, ProductItem product,
													 LanguageItem language) throws Exception {

		Validate.notNull(category, "CategoryItem cannot be null");
		Validate.notNull(product, "ProductItem cannot be null");

		product.getCategories().remove(category);
		productService.update(product);

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, product.getMerchantStore(), language);

		return readableProduct;
	}

	@Override
	public ReadableProduct getProductByCode(MerchantStoreItem store, String uniqueCode, LanguageItem language) {

		ProductItem product = productService.getByCode(uniqueCode);


		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, product.getMerchantStore(), language);

		return readableProduct;
	}

	@Override
	public void saveOrUpdateReview(PersistableProductReview review, MerchantStoreItem store,
								   LanguageItem language) throws Exception {
		PersistableProductReviewPopulator populator = new PersistableProductReviewPopulator();
		populator.setLanguageService(languageService);
		populator.setCustomerService(customerService);
		populator.setProductService(productService);

		ProductReviewItem rev =
				new ProductReviewItem();
		populator.populate(review, rev, store, language);

		if (review.getUuid() == null) {
			productReviewService.create(rev);
		} else {
			productReviewService.update(rev);
		}


		review.setUuid(rev.getUuid());

	}

	@Override
	public void deleteReview(ProductReviewItem review, MerchantStoreItem store, LanguageItem language)
			throws Exception {
		productReviewService.delete(review);

	}

	@Override
	public List<ReadableProductReview> getProductReviews(ProductItem product, MerchantStoreItem store,
														 LanguageItem language) throws Exception {


		List<ProductReviewItem> reviews = productReviewService.getByProduct(product);

		ReadableProductReviewPopulator populator = new ReadableProductReviewPopulator();

		List<ReadableProductReview> productReviews = new ArrayList<ReadableProductReview>();

		for (ProductReviewItem review : reviews) {
			ReadableProductReview readableReview = new ReadableProductReview();
			populator.populate(review, readableReview, store, language);
			productReviews.add(readableReview);
		}


		return productReviews;
	}

	@Override
	public void saveOrUpdateManufacturer(PersistableManufacturer manufacturer, MerchantStoreItem store,
										 LanguageItem language) throws Exception {

		PersistableManufacturerPopulator populator = new PersistableManufacturerPopulator();
		populator.setLanguageService(languageService);


		ManufacturerItem manuf = new ManufacturerItem();
		populator.populate(manufacturer, manuf, store, language);

		manufacturerService.saveOrUpdate(manuf);

		manufacturer.setUuid(manuf.getUuid());

	}

	@Override
	public void deleteManufacturer(ManufacturerItem manufacturer, MerchantStoreItem store, LanguageItem language)
			throws Exception {
		manufacturerService.delete(manufacturer);

	}

	@Override
	public ReadableManufacturer getManufacturer(UUID id, MerchantStoreItem store, LanguageItem language)
			throws Exception {
		ManufacturerItem manufacturer = manufacturerService.getById(id);

		if (manufacturer == null) {
			return null;
		}

		ReadableManufacturer readableManufacturer = new ReadableManufacturer();

		ReadableManufacturerPopulator populator = new ReadableManufacturerPopulator();
		populator.populate(manufacturer, readableManufacturer, store, language);


		return readableManufacturer;
	}

	@Override
	public List<ReadableManufacturer> getAllManufacturers(MerchantStoreItem store, LanguageItem language)
			throws Exception {


		List<ManufacturerItem> manufacturers = manufacturerService.listByStore(store);
		ReadableManufacturerPopulator populator = new ReadableManufacturerPopulator();
		List<ReadableManufacturer> returnList = new ArrayList<ReadableManufacturer>();

		for (ManufacturerItem m : manufacturers) {

			ReadableManufacturer readableManufacturer = new ReadableManufacturer();
			populator.populate(m, readableManufacturer, store, language);
			returnList.add(readableManufacturer);
		}

		return returnList;
	}

	@Override
	public List<ReadableProduct> relatedItems(MerchantStoreItem store, ProductItem product, LanguageItem language)
			throws Exception {
		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);

		List<ProductRelationshipItem> relatedItems =
				productRelationshipService.getByType(store, product, ProductRelationshipType.RELATED_ITEM);
		if (relatedItems != null && relatedItems.size() > 0) {
			List<ReadableProduct> items = new ArrayList<ReadableProduct>();
			for (ProductRelationshipItem relationship : relatedItems) {
				ProductItem relatedProduct = relationship.getRelatedProduct();
				ReadableProduct proxyProduct =
						populator.populate(relatedProduct, new ReadableProduct(), store, language);
				items.add(proxyProduct);
			}
			return items;
		}
		return null;
	}

}
