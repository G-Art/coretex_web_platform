package com.coretex.commerce.admin.init.data;

import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.commerce.core.services.LocaleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Service("initializationDatabase")
public class InitializationDatabaseImpl implements InitializationDatabase {

	private final List<DataLoader> dataLoaders;

	@Resource
	private LocaleService localeService;

	public InitializationDatabaseImpl(List<DataLoader> dataLoaders) {
		this.dataLoaders = dataLoaders;
	}

	public boolean isEmpty() {
		return localeService.count() == 0;
	}

	@Transactional
	public void populate(String contextName)  {

		if(CollectionUtils.isNotEmpty(dataLoaders)){
			dataLoaders.stream()
					.sorted(Comparator.comparingInt(DataLoader::priority).reversed())
					.forEach(loader -> loader.load(contextName));
		}
	}

}
