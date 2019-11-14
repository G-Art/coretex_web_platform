package com.coretex.core.business.modules.cms.impl;

/**
 * Infinispan asset manager
 *
 * @author casams1
 */
public class StoreCacheManagerImpl extends CacheManagerImpl {


	private final static String NAMED_CACHE = "StoreRepository";
	private String root;


	public StoreCacheManagerImpl(String location, String root) {
		super.init(NAMED_CACHE+"_"+root, location);
		this.root = root;
	}


	@Override
	public String getRootName() {
		return root;
	}


	@Override
	public String getLocation() {
		return location;
	}


}

