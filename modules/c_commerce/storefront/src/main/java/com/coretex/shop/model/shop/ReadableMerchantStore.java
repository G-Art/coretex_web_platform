package com.coretex.shop.model.shop;

import java.io.Serializable;

import com.coretex.shop.model.content.ReadableImage;
import com.coretex.shop.model.entity.ReadableAudit;
import com.coretex.shop.model.entity.ReadableAuditable;
import com.coretex.shop.model.references.ReadableAddress;

public class ReadableMerchantStore extends MerchantStoreEntity implements ReadableAuditable, Serializable {


	private static final long serialVersionUID = 1L;

	private String currentUserLanguage;
	private ReadableAddress address;
	private ReadableImage logo;
	private ReadableAudit audit;


	public String getCurrentUserLanguage() {
		return currentUserLanguage;
	}

	public void setCurrentUserLanguage(String currentUserLanguage) {
		this.currentUserLanguage = currentUserLanguage;
	}

	public ReadableAddress getAddress() {
		return address;
	}

	public void setAddress(ReadableAddress address) {
		this.address = address;
	}

	public ReadableImage getLogo() {
		return logo;
	}

	public void setLogo(ReadableImage logo) {
		this.logo = logo;
	}

	@Override
	public void setReadableAudit(ReadableAudit audit) {
		this.audit = audit;

	}

	@Override
	public ReadableAudit getReadableAudit() {
		return this.audit;
	}


}
