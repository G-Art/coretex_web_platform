package com.coretex.shop.store.controller.category.facade;

import com.coretex.core.business.exception.ConversionException;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.catalog.category.PersistableCategory;
import com.coretex.shop.model.catalog.category.ReadableCategory;
import com.coretex.shop.populator.catalog.PersistableCategoryPopulator;
import com.coretex.shop.populator.catalog.ReadableCategoryPopulator;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;
import com.coretex.shop.store.controller.converter.Converter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value = "categoryFacade")
public class CategoryFacadeImpl implements CategoryFacade {

	@Resource
	private CategoryService categoryService;

	@Resource
	private LanguageService languageService;

	@Resource
	private PersistableCategoryPopulator persistableCatagoryPopulator;

	@Resource
	private Converter<CategoryItem, ReadableCategory> categoryReadableCategoryConverter;

	private static final String FEATURED_CATEGORY = "featured";

	@Override
	public List<ReadableCategory> getCategoryHierarchy(
			MerchantStoreItem store, int depth, LocaleItem language, String filter) {
		List<CategoryItem> categories = getCategories(store, depth, language, filter);

		List<ReadableCategory> readableCategories =
				categories.stream()
						.filter(categoryItem ->  BooleanUtils.toBoolean(categoryItem.getVisible()))
						.map(cat -> categoryReadableCategoryConverter.convert(cat, store, language))
						.collect(Collectors.toList());

		Map<UUID, ReadableCategory> readableCategoryMap =
				readableCategories.stream()
						.collect(Collectors.toMap(ReadableCategory::getUuid, Function.identity()));

		readableCategories.stream()
				.filter(category -> BooleanUtils.toBoolean( category.isVisible()))
				.filter(cat -> Objects.nonNull(cat.getParent()))
				.filter(cat -> readableCategoryMap.containsKey(cat.getParent().getUuid()))
				.forEach(
						readableCategory -> {
							ReadableCategory parentCategory =
									readableCategoryMap.get(readableCategory.getParent().getUuid());
							if (parentCategory != null) {
								parentCategory.getChildren().add(readableCategory);
							}
						});

		return readableCategoryMap.values().stream()
				.filter(cat -> cat.getDepth() == null || cat.getDepth() == 0)
				.sorted(Comparator.comparing(ReadableCategory::getSortOrder))
				.collect(Collectors.toList());
	}

	private List<CategoryItem> getCategories(
			MerchantStoreItem store, int depth, LocaleItem language, String filter) {
		if (StringUtils.isNotBlank(filter) && FEATURED_CATEGORY.equals(filter)) {
			return categoryService.getListByDepthFilterByFeatured(store, depth, language);
		} else {
			return categoryService.getListByDepth(store, depth, language);
		}
	}

	@Override
	public PersistableCategory saveCategory(MerchantStoreItem store, PersistableCategory category) {

      /*		PersistableCategoryPopulator populator = new PersistableCategoryPopulator();
      populator.setCategoryService(categoryService);
      populator.setLanguageService(languageService);*/

		CategoryItem target =
				Optional.ofNullable(category.getUuid())
						.map(categoryService::getByUUID)
						.orElse(new CategoryItem());

		CategoryItem dbCategory = populateCategory(store, category, target);

		saveCategory(store, dbCategory, null);

		// set category id
		category.setUuid(dbCategory.getUuid());
		return category;
	}

	private CategoryItem populateCategory(
			MerchantStoreItem store, PersistableCategory category, CategoryItem target) {
		try {
			return persistableCatagoryPopulator.populate(
					category, target, store, store.getDefaultLanguage());
		} catch (ConversionException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	private void saveCategory(MerchantStoreItem store, CategoryItem category, CategoryItem parent)
			 {

		/**
		 * c.children1
		 *
		 * <p>children1.children1 children1.children2
		 *
		 * <p>children1.children2.children1
		 */

		/** set lineage * */
		if (parent != null) {
			category.setParent(category);

			String lineage = parent.getLineage();
			int depth = parent.getDepth();

			category.setDepth(depth + 1);
			category.setLineage(
					new StringBuilder().append(lineage).append(parent.getUuid()).append("/").toString());
		}

		category.setMerchantStore(store);

		// remove children
		List<CategoryItem> children = category.getCategories();
		category.setCategories(null);

		/** set parent * */
		if (parent != null) {
			category.setParent(parent);
		}

		categoryService.saveOrUpdate(category);

		if (!CollectionUtils.isEmpty(children)) {
			parent = category;
			for (CategoryItem sub : children) {

				saveCategory(store, sub, parent);
			}
		}
	}

	@Override
	public ReadableCategory getById(MerchantStoreItem store, UUID id, LocaleItem language) {
		CategoryItem categoryModel = getCategoryById(id, language);

		StringBuilder lineage =
				new StringBuilder().append(categoryModel.getLineage()).append(categoryModel.getUuid());

		// get children
		ReadableCategory readableCategory =
				categoryReadableCategoryConverter.convert(categoryModel, store, language);

		List<CategoryItem> children = getListByLineage(store, lineage.toString());

		List<ReadableCategory> childrenCats =
				children.stream()
						.map(cat -> categoryReadableCategoryConverter.convert(cat, store, language))
						.collect(Collectors.toList());

		addChildToParent(readableCategory, childrenCats);
		return readableCategory;
	}

	private void addChildToParent(ReadableCategory readableCategory,
								  List<ReadableCategory> childrenCats) {
		Map<UUID, ReadableCategory> categoryMap =
				childrenCats.stream()
						.collect(Collectors.toMap(ReadableCategory::getUuid, Function.identity()));
		categoryMap.put(readableCategory.getUuid(), readableCategory);

		// traverse map and add child to parent
		for (ReadableCategory readable : childrenCats) {

			if (readable.getParent() != null) {

				ReadableCategory rc = categoryMap.get(readable.getParent().getUuid());
				rc.getChildren().add(readable);
			}
		}
	}

	private List<CategoryItem> getListByLineage(MerchantStoreItem store, String lineage) {
		return categoryService.getListByLineage(store, lineage);
	}

	private CategoryItem getCategoryById(UUID id, LocaleItem language) {
		return Optional.ofNullable(categoryService.getOneByLanguage(id, language))
				.orElseThrow(() -> new ResourceNotFoundException("CategoryItem id not found"));
	}

	@Override
	public void deleteCategory(CategoryItem category) {
		categoryService.delete(category);
	}

	@Override
	public ReadableCategory getByCode(MerchantStoreItem store, String code, LocaleItem language)
			throws Exception {

		Validate.notNull(code, "category code must not be null");
		ReadableCategoryPopulator categoryPopulator = new ReadableCategoryPopulator();
		ReadableCategory readableCategory = new ReadableCategory();

		CategoryItem category = categoryService.getByCode(store, code);
		categoryPopulator.populate(category, readableCategory, store, language);

		return readableCategory;
	}

	@Override
	public void deleteCategory(UUID categoryId) {
		CategoryItem category = getOne(categoryId);
		deleteCategory(category);
	}

	private CategoryItem getOne(UUID categoryId) {
		return Optional.ofNullable(categoryService.getByUUID(categoryId))
				.orElseThrow(
						() ->
								new ResourceNotFoundException(
										String.format("No CategoryItem found for ID : %s", categoryId)));
	}
}
