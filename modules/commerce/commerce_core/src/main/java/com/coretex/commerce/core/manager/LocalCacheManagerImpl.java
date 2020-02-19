package com.coretex.commerce.core.manager;

public class LocalCacheManagerImpl implements CMSManager {

	private String rootName;// file location root

	public LocalCacheManagerImpl(String rootName) {
		this.rootName = rootName;
	}


	@Override
	public String getRootName() {
		return rootName;
	}

	@Override
	public String getLocation() {
		return "";
	}


}
