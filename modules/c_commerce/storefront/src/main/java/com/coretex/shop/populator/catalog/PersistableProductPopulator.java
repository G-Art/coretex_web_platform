package com.coretex.shop.populator.catalog;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.shop.model.catalog.product.PersistableImage;
import com.coretex.shop.model.catalog.product.PersistableProduct;
import com.coretex.shop.model.catalog.product.ProductPriceEntity;
import com.coretex.shop.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Date;


@Component
public class PersistableProductPopulator extends
		AbstractDataPopulator<PersistableProduct, ProductItem> {

	@Resource
	private CategoryService categoryService;
	@Resource
	private ManufacturerService manufacturerService;
	@Resource
	private LanguageService languageService;
	@Resource
	private CustomerService customerService;


	@Override
	public ProductItem populate(PersistableProduct source,
								ProductItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {

		try {

			target.setSku(source.getSku());
			target.setAvailable(source.isAvailable());
			target.setPreOrder(source.isPreOrder());
			if (source.getUuid() != null) {
				target.setUuid(null);
			} else {
				target.setUuid(source.getUuid());
			}

			target.setCondition(source.getCondition());


			//RENTAL
			target.setRentalDuration(source.getRentalDuration());
			target.setRentalStatus(source.getRentalStatus());
			target.setRentalPeriod(source.getRentalPeriod());

			/** end RENTAL **/

			if (source.getOwner() != null && source.getOwner().getUuid() != null) {
				CustomerItem owner = customerService.getByUUID(source.getOwner().getUuid());
				target.setOwner(owner);
			}

			if (!StringUtils.isBlank(source.getDateAvailable())) {
				target.setDateAvailable(DateUtil.getDate(source.getDateAvailable()));
			}

			if (source.getManufacturer() != null) {

				ManufacturerItem manuf = null;
				if (!StringUtils.isBlank(source.getManufacturer().getCode())) {
					manuf = manufacturerService.getByCode(store, source.getManufacturer().getCode());
				} else {
					Validate.notNull(source.getManufacturer().getUuid(), "Requires to set manufacturer id");
					manuf = manufacturerService.getByUUID(source.getManufacturer().getUuid());
				}

				if (manuf == null) {
					throw new ConversionException("Invalid manufacturer id");
				}
				if (manuf != null) {
					if (!manuf.getMerchantStore().getUuid().equals(store.getUuid())) {
						throw new ConversionException("Invalid manufacturer id");
					}
					target.setManufacturer(manuf);
				}
			}

			target.setMerchantStore(store);


			//target.setType(source.getType());//not implemented yet
			target.setProductHeight(source.getProductHeight());
			target.setProductLength(source.getProductLength());
			target.setProductWeight(source.getProductWeight());
			target.setProductWidth(source.getProductWidth());
			target.setSortOrder(source.getSortOrder());
			target.setProductVirtual(source.isProductVirtual());
			target.setProductShippable(source.isProductShipeable());
			if (source.getRating() != null) {
				target.setProductReviewAvg(new BigDecimal(source.getRating()));
			}
			target.setProductReviewCount(source.getRatingCount());


			if (CollectionUtils.isNotEmpty(source.getProductPrices())) {

				ProductAvailabilityItem productAvailability = new ProductAvailabilityItem();
				
/*				if(productAvailability.getUuid() != null && productAvailability.getUuid().longValue() == 0) {
				} else {
					productAvailability.setUuid(null);
				}*/

				productAvailability.setProductQuantity(source.getQuantity());
				productAvailability.setProduct(target);
				productAvailability.setProductQuantityOrderMin(1);
				productAvailability.setProductQuantityOrderMax(1);

				for (ProductPriceEntity priceEntity : source.getProductPrices()) {

					ProductPriceItem price = new ProductPriceItem();
					price.setProductAvailability(productAvailability);
					price.setDefaultPrice(priceEntity.isDefaultPrice());
					price.setProductPriceAmount(priceEntity.getOriginalPrice());
					price.setCode(priceEntity.getCode());
					price.setProductPriceSpecialAmount(priceEntity.getDiscountedPrice());
					if (priceEntity.getDiscountStartDate() != null) {
						Date startDate = DateUtil.getDate(priceEntity.getDiscountStartDate());
						price.setProductPriceSpecialStartDate(startDate);
					}
					if (priceEntity.getDiscountEndDate() != null) {
						Date endDate = DateUtil.getDate(priceEntity.getDiscountEndDate());
						price.setProductPriceSpecialEndDate(endDate);
					}
					productAvailability.getPrices().add(price);
					target.getAvailabilities().add(productAvailability);

					price.setName(Constants.DEFAULT_PRICE_DESCRIPTION);
				}

			} else { //create 

				ProductAvailabilityItem productAvailability = new ProductAvailabilityItem();
				productAvailability.setProduct(target);
				productAvailability.setProductQuantity(source.getQuantity());
				productAvailability.setProductQuantityOrderMin(1);
				productAvailability.setProductQuantityOrderMax(1);

				ProductPriceItem price = new ProductPriceItem();
				price.setDefaultPrice(true);
				price.setProductPriceAmount(source.getPrice());
				price.setCode(ProductPriceEntity.DEFAULT_PRICE_CODE);
				price.setProductAvailability(productAvailability);
				productAvailability.getPrices().add(price);
				target.getAvailabilities().add(productAvailability);
				price.setName(Constants.DEFAULT_PRICE_DESCRIPTION);
			}


			//image
			if (source.getImages() != null) {
				for (PersistableImage img : source.getImages()) {
					ByteArrayInputStream in = new ByteArrayInputStream(img.getBytes());
					ProductImageItem productImage = new ProductImageItem();
					productImage.setProduct(target);
					productImage.setProductImage(img.getName());
					target.getImages().add(productImage);
				}
			}

			//attributes
			if (source.getAttributes() != null) {
				for (com.coretex.shop.model.catalog.product.attribute.PersistableProductAttribute attr : source.getAttributes()) {

					ProductAttributeItem attribute = new ProductAttributeItem();
					attribute.setProduct(target);
					attribute.setProductAttributePrice(attr.getProductAttributePrice());
					attribute.setProductAttributeWeight(attr.getProductAttributeWeight());
					attribute.setProductAttributePrice(attr.getProductAttributePrice());
					target.getAttributes().add(attribute);

				}
			}


			//categories
			if (!CollectionUtils.isEmpty(source.getCategories())) {
				for (com.coretex.shop.model.catalog.category.Category categ : source.getCategories()) {

					CategoryItem c = null;
					if (!StringUtils.isBlank(categ.getCode())) {
						c = categoryService.getByCode(store, categ.getCode());
					} else {
						Validate.notNull(categ.getUuid(), "CategoryItem id nust not be null");
						c = categoryService.getByUUID(categ.getUuid());
					}

					if (c == null) {
						throw new ConversionException("CategoryItem id " + categ.getUuid() + " does not exist");
					}
					if (!c.getMerchantStore().getUuid().equals(store.getUuid())) {
						throw new ConversionException("Invalid category id");
					}
					target.getCategories().add(c);
				}
			}
			return target;

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setManufacturerService(ManufacturerService manufacturerService) {
		this.manufacturerService = manufacturerService;
	}

	public ManufacturerService getManufacturerService() {
		return manufacturerService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	@Override
	protected ProductItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}


	public CustomerService getCustomerService() {
		return customerService;
	}


	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

}
