package com.coretex.core.business.services.catalog.product.file;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.DigitalProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.modules.cms.content.StaticContentFileManager;
import com.coretex.core.business.repositories.catalog.product.file.DigitalProductDao;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.model.content.FileContentType;
import com.coretex.core.model.content.InputContentFile;

@Service("digitalProductService")
public class DigitalProductServiceImpl extends SalesManagerEntityServiceImpl<DigitalProductItem>
		implements DigitalProductService {


	private DigitalProductDao digitalProductDao;

	@Resource
	StaticContentFileManager productDownloadsFileManager;

	@Resource
	ProductService productService;

	public DigitalProductServiceImpl(DigitalProductDao digitalProductDao) {
		super(digitalProductDao);
		this.digitalProductDao = digitalProductDao;
	}

	@Override
	public void addProductFile(ProductItem product, DigitalProductItem digitalProduct, InputContentFile inputFile) throws ServiceException {

		Assert.notNull(digitalProduct, "DigitalProductItem cannot be null");
		Assert.notNull(product, "ProductItem cannot be null");
		digitalProduct.setProduct(product);

		try {

			Assert.notNull(inputFile.getFile(), "InputContentFile.file cannot be null");

			Assert.notNull(product.getMerchantStore(), "ProductItem.merchantStore cannot be null");
			this.saveOrUpdate(digitalProduct);

			productDownloadsFileManager.addFile(product.getMerchantStore().getCode(), inputFile);

			product.setProductVirtual(true);
			productService.update(product);

		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {

				if (inputFile.getFile() != null) {
					inputFile.getFile().close();
				}

			} catch (Exception ignore) {
			}
		}


	}

	@Override
	public DigitalProductItem getByProduct(MerchantStoreItem store, ProductItem product) {
		return digitalProductDao.findByProduct(store.getUuid(), product.getUuid());
	}

	@Override
	public void delete(DigitalProductItem digitalProduct) {

		Assert.notNull(digitalProduct, "DigitalProductItem cannot be null");
		Assert.notNull(digitalProduct.getProduct(), "DigitalProductItem.product cannot be null");
		//refresh file
		digitalProduct = this.getByUUID(digitalProduct.getUuid());
		super.delete(digitalProduct);
		productDownloadsFileManager.removeFile(digitalProduct.getProduct().getMerchantStore().getCode(), FileContentType.PRODUCT, digitalProduct.getProductFileName());
		digitalProduct.getProduct().setProductVirtual(false);
		productService.update(digitalProduct.getProduct());
	}


	@Override
	public void saveOrUpdate(DigitalProductItem digitalProduct) throws ServiceException {

		Assert.notNull(digitalProduct, "DigitalProductItem cannot be null");
		Assert.notNull(digitalProduct.getProduct(), "DigitalProductItem.product cannot be null");
		super.save(digitalProduct);

		digitalProduct.getProduct().setProductVirtual(true);
		productService.update(digitalProduct.getProduct());


	}


}
