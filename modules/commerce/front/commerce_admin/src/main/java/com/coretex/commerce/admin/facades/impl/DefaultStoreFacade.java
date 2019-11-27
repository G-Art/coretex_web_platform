package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.commerce.admin.data.MinimalMerchantStoreData;
import com.coretex.commerce.admin.facades.StoreFacade;
import com.coretex.commerce.admin.mapper.MerchantStoreDataMapper;
import com.coretex.commerce.admin.mapper.MinimalMerchantStoreDataMapper;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("storeFacade")
public class DefaultStoreFacade implements StoreFacade {

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private MerchantStoreDataMapper merchantStoreDataMapper;

	@Resource
	private MinimalMerchantStoreDataMapper minimalMerchantStoreDataMapper;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultStoreFacade.class);

	@Override
	public MerchantStoreData getByCode(String code) {
		return merchantStoreDataMapper.fromItem(merchantStoreService.getByCode(code));
	}

	@Override
	public List<MerchantStoreData> getAll() {
		return merchantStoreService.list()
				.stream()
				.map(merchantStoreDataMapper::fromItem)
				.collect(Collectors.toList());
	}

	@Override
	public DataTableResults<MinimalMerchantStoreData> tableResult(String draw, long page, Long length) {
		var pageableList = merchantStoreService.pageableList(length, page);
		var dataTableResults = new DataTableResults<MinimalMerchantStoreData>();
		dataTableResults.setDraw(draw);
		dataTableResults.setRecordsTotal(String.valueOf(pageableList.getTotalCount()));
		dataTableResults.setRecordsFiltered(String.valueOf(pageableList.getCount()));
		dataTableResults.setListOfDataObjects(pageableList.getResult()
				.stream()
				.map(minimalMerchantStoreDataMapper::fromItem)
				.collect(Collectors.toList()));
		return dataTableResults;
	}

}
