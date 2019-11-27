package com.coretex.shop.populator.catalog;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.model.catalog.category.CategoryDescription;
import com.coretex.shop.model.catalog.category.PersistableCategory;

@Component
public class PersistableCategoryPopulator extends
		AbstractDataPopulator<PersistableCategory, CategoryItem> {

	@Resource
	private CategoryService categoryService;
	@Resource
	private LanguageService languageService;


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}


	@Override
	public CategoryItem populate(PersistableCategory source, CategoryItem target,
								 MerchantStoreItem store, LocaleItem language)
			throws ConversionException {

		try {

		
/*		Validate.notNull(categoryService, "Requires to set CategoryService");
		Validate.notNull(languageService, "Requires to set LanguageService");*/

			target.setMerchantStore(store);
			target.setCode(source.getCode());
			target.setSortOrder(source.getSortOrder());
			target.setVisible(source.isVisible());
			target.setFeatured(source.isFeatured());

			//get parent

			if (source.getParent() == null) {

				target.setParent(null);
				target.setLineage("/");
				target.setDepth(0);

			} else {
				CategoryItem parent = null;
				if (!StringUtils.isBlank(source.getParent().getCode())) {
					parent = categoryService.getByCode(store.getCode(), source.getParent().getCode());
				} else if (source.getParent().getUuid() != null) {
					parent = categoryService.getByUUID(source.getParent().getUuid());
				} else {
					throw new ConversionException("CategoryItem parent needs at least an id or a code for reference");
				}
				if (parent != null && !parent.getMerchantStore().getUuid().equals(store.getUuid())) {
					throw new ConversionException("Store id does not belong to specified parent id");
				}

				if (parent != null) {
					target.setParent(parent);

					String lineage = parent.getLineage();
					int depth = parent.getDepth();

					target.setDepth(depth + 1);
					target.setLineage(new StringBuilder().append(lineage).append(parent.getUuid()).append("/").toString());
				}

			}


			if (!CollectionUtils.isEmpty(source.getChildren())) {

				for (PersistableCategory cat : source.getChildren()) {

					CategoryItem persistCategory = this.populate(cat, new CategoryItem(), store, language);
					target.getCategories().add(persistCategory);

				}

			}


			if (!CollectionUtils.isEmpty(source.getDescriptions())) {
				for (CategoryDescription description : source.getDescriptions()) {
					LocaleItem lang = languageService.getByCode(description.getLanguage());
					if (lang == null) {
						throw new ConversionException("LocaleItem is null for code " + description.getLanguage() + " use language ISO code [en, fr ...]");
					}
					target.setCategoryHighlight(description.getHighlights());
					target.setDescription(description.getDescription());
					target.setName(description.getName());
					target.setMetatagDescription(description.getMetaDescription());
					target.setMetatagTitle(description.getTitle());
					target.setSeUrl(description.getFriendlyUrl());
				}
			}


			return target;


		} catch (Exception e) {
			throw new ConversionException(e);
		}

	}


	@Override
	protected CategoryItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
