package com.coretex.shop.model.shop;

import java.util.ArrayList;
import java.util.List;

public class MerchantStoreBrand {


	private List<MerchantConfigEntity> socialNetworks = new ArrayList<MerchantConfigEntity>();

	public List<MerchantConfigEntity> getSocialNetworks() {
		return socialNetworks;
	}

	public void setSocialNetworks(List<MerchantConfigEntity> socialNetworks) {
		this.socialNetworks = socialNetworks;
	}

}
