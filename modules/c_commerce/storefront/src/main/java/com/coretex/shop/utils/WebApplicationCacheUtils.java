package com.coretex.shop.utils;

import com.coretex.core.business.utils.CacheUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebApplicationCacheUtils {

	@Resource
	private CacheUtils cache;

	public Object getFromCache(String key) throws Exception {
		return cache.getFromCache(key);
	}

	public void putInCache(String key, Object object) throws Exception {
		cache.putInCache(object, key);
	}

}
