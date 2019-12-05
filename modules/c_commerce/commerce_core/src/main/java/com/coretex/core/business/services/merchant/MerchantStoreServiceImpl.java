package com.coretex.core.business.services.merchant;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Service;


import com.coretex.core.business.repositories.merchant.MerchantDao;
import com.coretex.core.business.services.catalog.product.type.ProductTypeService;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.core.model.merchant.MerchantStoreCriteria;

@Service("merchantService")
public class MerchantStoreServiceImpl extends SalesManagerEntityServiceImpl<MerchantStoreItem>
		implements MerchantStoreService {


	@Resource
	protected ProductTypeService productTypeService;


	private MerchantDao merchantDao;

	public MerchantStoreServiceImpl(MerchantDao merchantDao) {
		super(merchantDao);
		this.merchantDao = merchantDao;
	}


	@Override
	public void saveOrUpdate(MerchantStoreItem store)  {

		super.save(store);

	}


	@Override
	public MerchantStoreItem getByCode(String code) {
		return merchantDao.findByCode(code);
	}

	@Override
	public boolean existByCode(String code) {
		return merchantDao.existsByCode(code);
	}


	@Override
	public GenericEntityList<MerchantStoreItem> getByCriteria(MerchantStoreCriteria criteria)  {
		return merchantDao.listByCriteria(criteria);
	}

	
/*	@Override
	public void delete(MerchantStoreItem merchant)  {
		
		merchant = this.getById(merchant.getUuid());
		
		
		//reference
		List<ManufacturerItem> manufacturers = manufacturerService.listByStore(merchant);
		for(ManufacturerItem manufacturer : manufacturers) {
			manufacturerService.delete(manufacturer);
		}
		
		List<MerchantConfigurationItem> configurations = merchantConfigurationService.listByStore(merchant);
		for(MerchantConfigurationItem configuration : configurations) {
			merchantConfigurationService.delete(configuration);
		}
		

		//TODO taxService
		List<TaxClassItem> taxClasses = taxClassService.listByStore(merchant);
		for(TaxClassItem taxClass : taxClasses) {
			taxClassService.delete(taxClass);
		}
		
		//content
		contentService.removeFiles(merchant.getCode());
		//TODO staticContentService.removeImages
		
		//category / product
		List<CategoryItem> categories = categoryService.listByStore(merchant);
		for(CategoryItem category : categories) {
			categoryService.delete(category);
		}

		//users
		List<UserItem> users = userService.listByStore(merchant);
		for(UserItem user : users) {
			userService.delete(user);
		}
		
		//customers
		List<CustomerItem> customers = customerService.listByStore(merchant);
		for(CustomerItem customer : customers) {
			customerService.delete(customer);
		}
		
		//orders
		List<OrderItem> orders = orderService.listByStore(merchant);
		for(OrderItem order : orders) {
			orderService.delete(order);
		}
		
		super.delete(merchant);
		
	}*/

}
