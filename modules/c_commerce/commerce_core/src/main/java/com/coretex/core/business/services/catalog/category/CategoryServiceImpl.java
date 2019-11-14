package com.coretex.core.business.services.catalog.category;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.catalog.category.CategoryDao;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service("categoryService")
public class CategoryServiceImpl extends SalesManagerEntityServiceImpl<CategoryItem> implements CategoryService {

	private CategoryDao categoryDao;


	@Resource
	private ProductService productService;


	public CategoryServiceImpl(CategoryDao categoryDao) {
		super(categoryDao);
		this.categoryDao = categoryDao;
	}

	public void create(CategoryItem category) {

		super.create(category);

		StringBuilder lineage = new StringBuilder();
		CategoryItem parent = category.getParent();
		if (parent != null && parent.getUuid() != null) {
			lineage.append(parent.getLineage()).append("/").append(parent.getUuid());
			category.setDepth(parent.getDepth() + 1);
		} else {
			lineage.append("/");
			category.setDepth(0);
		}
		category.setLineage(lineage.toString());
		super.update(category);


	}

	@Override
	public List<Map<String, Object>> countProductsByCategories(MerchantStoreItem store,
															   List<UUID> categoryIds) {

		return categoryDao.countProductsByCategories(store, categoryIds);

	}

	@Override
	public List<CategoryItem> listByCodes(MerchantStoreItem store, List<String> codes,
										  LanguageItem language) {
		return categoryDao.findByCodes(store.getUuid(), codes, language.getUuid());
	}

	@Override
	public List<CategoryItem> listByIds(MerchantStoreItem store, List<UUID> ids,
										LanguageItem language) {
		return categoryDao.findByIds(store.getUuid(), ids, language.getUuid());
	}

	@Override
	public CategoryItem getOneByLanguage(UUID categoryId, LanguageItem language) {
		return categoryDao.findById(categoryId, language.getUuid());
	}

	@Override
	public void saveOrUpdate(CategoryItem category) throws ServiceException {


		//save or update (persist and attach entities
		if (category.getUuid() != null) {

			super.update(category);

		} else {

			super.save(category);

		}

	}

	@Override
	public List<CategoryItem> getListByLineage(MerchantStoreItem store, String lineage) {
		return categoryDao.findByLineage(store.getUuid(), lineage);
	}

	@Override
	public List<CategoryItem> getListByLineage(String storeCode, String lineage) throws ServiceException {
		try {
			return categoryDao.findByLineage(storeCode, lineage);
		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}


	@Override
	public List<CategoryItem> listBySeUrl(MerchantStoreItem store, String seUrl) throws ServiceException {

		try {
			return categoryDao.listByFriendlyUrl(store.getUuid(), seUrl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public CategoryItem getBySeUrl(MerchantStoreItem store, String seUrl) {
		return categoryDao.findByFriendlyUrl(store.getUuid(), seUrl);
	}


	@Override
	public CategoryItem getByCode(MerchantStoreItem store, String code) throws ServiceException {

		try {
			return categoryDao.findByCode(store.getUuid(), code);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public CategoryItem getByCode(String storeCode, String code) throws ServiceException {

		try {
			return categoryDao.findByCode(storeCode, code);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<CategoryItem> listByParent(CategoryItem category) throws ServiceException {

		try {
			return categoryDao.listByStoreAndParent(null, category);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<CategoryItem> listByStoreAndParent(MerchantStoreItem store, CategoryItem category) throws ServiceException {

		try {
			return categoryDao.listByStoreAndParent(store, category);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<CategoryItem> listByParent(CategoryItem category, LanguageItem language) {
		Assert.notNull(category, "CategoryItem cannot be null");
		Assert.notNull(language, "LanguageItem cannot be null");
		Assert.notNull(category.getMerchantStore(), "category.merchantStore cannot be null");

		return categoryDao.findByParent(category.getUuid(), language.getUuid());
	}


	//@Override
	public void delete(CategoryItem category) {

		//get category with lineage (subcategories)
		StringBuilder lineage = new StringBuilder();
		lineage.append(category.getLineage()).append(category.getUuid()).append(Constants.SLASH);
		List<CategoryItem> categories = this.getListByLineage(category.getMerchantStore(), lineage.toString());

		CategoryItem dbCategory = this.getById(category.getUuid());


		if (dbCategory != null && dbCategory.getUuid().equals(category.getUuid())) {


			categories.add(dbCategory);


			Collections.reverse(categories);

			List<UUID> categoryIds = new ArrayList<>();


			for (CategoryItem c : categories) {
				categoryIds.add(c.getUuid());
			}

			List<ProductItem> products = productService.getProducts(categoryIds);

			for (ProductItem product : products) {
				ProductItem dbProduct = productService.getById(product.getUuid());
				Set<CategoryItem> productCategories = dbProduct.getCategories();
				if (productCategories.size() > 1) {
					for (CategoryItem c : categories) {
						productCategories.remove(c);
						productService.update(dbProduct);
					}

					if (product.getCategories() == null || product.getCategories().size() == 0) {
						productService.delete(dbProduct);
					}

				} else {
					productService.delete(dbProduct);
				}


			}

			CategoryItem categ = this.getById(category.getUuid());
			categoryDao.delete(categ);

		}

	}


	@Override
	public void addChild(CategoryItem parent, CategoryItem child) throws ServiceException {


		if (child == null || child.getMerchantStore() == null) {
			throw new ServiceException("Child category and merchant store should not be null");
		}

		try {

			if (parent == null) {

				//assign to root
				child.setParent(null);
				child.setDepth(0);
				//child.setLineage(new StringBuilder().append("/").append(child.getUuid()).append("/").toString());
				child.setLineage("/");

			} else {

				CategoryItem p = this.getById(parent.getUuid());//parent


				String lineage = p.getLineage();
				int depth = p.getDepth();//TODO sometimes null

				child.setParent(p);
				child.setDepth(depth + 1);
				child.setLineage(new StringBuilder().append(lineage).append(p.getUuid()).append("/").toString());


			}


			update(child);
			StringBuilder childLineage = new StringBuilder();
			childLineage.append(child.getLineage()).append(child.getUuid()).append("/");
			List<CategoryItem> subCategories = getListByLineage(child.getMerchantStore(), childLineage.toString());


			//ajust all sub categories lineages
			if (subCategories != null && subCategories.size() > 0) {
				for (CategoryItem subCategory : subCategories) {
					if (child.getUuid() != subCategory.getUuid()) {
						addChild(child, subCategory);
					}
				}

			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}

	@Override
	public List<CategoryItem> getListByDepth(MerchantStoreItem store, int depth) {
		return categoryDao.findByDepth(store.getUuid(), depth);
	}

	@Override
	public List<CategoryItem> getListByDepth(MerchantStoreItem store, int depth, LanguageItem language) {
		return categoryDao.findByDepth(store.getUuid(), depth, language.getUuid());
	}

	@Override
	public List<CategoryItem> getListByDepthFilterByFeatured(MerchantStoreItem store, int depth, LanguageItem language) {
		return categoryDao.findByDepthFilterByFeatured(store.getUuid(), depth, language.getUuid());
	}

	@Override
	public List<CategoryItem> getByName(MerchantStoreItem store, String name, LanguageItem language) throws ServiceException {

		try {
			return categoryDao.findByName(store.getUuid(), name, language.getUuid());
		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}


	@Override
	public List<CategoryItem> listByStore(MerchantStoreItem store)
			throws ServiceException {

		try {
			return categoryDao.findByStore(store.getUuid());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<CategoryItem> listByStore(MerchantStoreItem store, LanguageItem language)
			throws ServiceException {

		try {
			return categoryDao.findByStore(store.getUuid(), language.getUuid());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


}
