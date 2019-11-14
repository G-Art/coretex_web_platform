package com.coretex.core.business.utils;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component("cacheUtil")
public class CacheUtils {


	@Resource
	@Qualifier("serviceCache")
	private Cache cache;


	public final static String REFERENCE_CACHE = "REF";

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);

	private final static String KEY_DELIMITER = "_";


	public void putInCache(Object object, String keyName) throws Exception {

		cache.put(keyName, object);

	}


	public Object getFromCache(String keyName) throws Exception {

		ValueWrapper vw = cache.get(keyName);
		if (vw != null) {
			return vw.get();
		}

		return null;

	}

	public List<String> getCacheKeys(MerchantStoreItem store) throws Exception {

		net.sf.ehcache.Cache cacheImpl = (net.sf.ehcache.Cache) cache.getNativeCache();
		List<String> returnKeys = new ArrayList<String>();
		for (Object key : cacheImpl.getKeys()) {


			try {
				String sKey = (String) key;

				// a key should be <storeId>_<rest of the key>
				int delimiterPosition = sKey.indexOf(KEY_DELIMITER);

				if (delimiterPosition > 0 && Character.isDigit(sKey.charAt(0))) {

					String keyRemaining = sKey.substring(delimiterPosition + 1);
					returnKeys.add(keyRemaining);

				}

			} catch (Exception e) {
				LOGGER.equals("key " + key + " cannot be converted to a String or parsed");
			}
		}

		return returnKeys;
	}

	public void shutDownCache() throws Exception {

	}

	public void removeFromCache(String keyName) throws Exception {
		cache.evict(keyName);
	}

	public void removeAllFromCache(MerchantStoreItem store) throws Exception {
		net.sf.ehcache.Cache cacheImpl = (net.sf.ehcache.Cache) cache.getNativeCache();
		for (Object key : cacheImpl.getKeys()) {
			try {
				String sKey = (String) key;

				// a key should be <storeId>_<rest of the key>
				int delimiterPosition = sKey.indexOf(KEY_DELIMITER);

				if (delimiterPosition > 0 && Character.isDigit(sKey.charAt(0))) {


					cache.evict(key);

				}

			} catch (Exception e) {
				LOGGER.equals("key " + key + " cannot be converted to a String or parsed");
			}
		}
	}


}
