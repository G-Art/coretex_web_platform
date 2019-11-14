package com.coretex.shop.model.shop;

import com.coretex.shop.model.references.PersistableAddress;

public class PersistableMerchantStore extends MerchantStoreEntity {


	private static final long serialVersionUID = 1L;
	private PersistableAddress address;

	public PersistableAddress getAddress() {
		return address;
	}

	public void setAddress(PersistableAddress address) {
		this.address = address;
	}

}
