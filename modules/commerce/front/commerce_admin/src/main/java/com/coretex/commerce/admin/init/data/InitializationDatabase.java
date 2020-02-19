package com.coretex.commerce.admin.init.data;

public interface InitializationDatabase {

	boolean isEmpty();

	void populate(String name) ;
}
