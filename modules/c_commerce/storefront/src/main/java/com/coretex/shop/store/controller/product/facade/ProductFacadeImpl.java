package com.coretex.shop.store.controller.product.facade;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.populator.manufacturer.ReadableManufacturerPopulator;
import com.coretex.shop.utils.ImageFilePath;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service("productFacade")
public class ProductFacadeImpl implements ProductFacade {

	@Resource
	private ManufacturerService manufacturerService;

//	@Resource
//	private ProductService productService;

	@Resource
	private PricingService pricingService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Override
	public ReadableProduct getProduct(MerchantStoreItem store, UUID id, LocaleItem language){

//		ProductItem product = productService.getByUUID(id);

//		if (product == null) {
//			return null;
//		}

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
//		populator.populate(product, readableProduct, store, language);

		return readableProduct;
	}

	@Override
	public ReadableProduct getProduct(MerchantStoreItem store, String sku, LocaleItem language)
			throws Exception {

//		ProductItem product = productService.getByCode(sku);

//		if (product == null) {
//			return null;
//		}

		ReadableProduct readableProduct = new ReadableProduct();

		ReadableProductPopulator populator = new ReadableProductPopulator();

		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
//		populator.populate(product, readableProduct, store, language);

		return readableProduct;
	}

	@Override
	public ReadableManufacturer getManufacturer(UUID id, MerchantStoreItem store, LocaleItem language)
			throws Exception {
		ManufacturerItem manufacturer = manufacturerService.getByUUID(id);

		if (manufacturer == null) {
			return null;
		}

		ReadableManufacturer readableManufacturer = new ReadableManufacturer();

		ReadableManufacturerPopulator populator = new ReadableManufacturerPopulator();
		populator.populate(manufacturer, readableManufacturer, store, language);


		return readableManufacturer;
	}

}
