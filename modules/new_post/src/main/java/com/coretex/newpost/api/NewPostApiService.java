package com.coretex.newpost.api;

import com.coretex.newpost.api.actions.NewPostActionApiService;
import com.coretex.newpost.api.address.NewPostAddressApiService;
import com.coretex.newpost.api.enchiridion.NewPostEnchiridionsApiService;

public interface NewPostApiService {
	NewPostAddressApiService getNewPostAddressApiService();

	NewPostEnchiridionsApiService getNewPostEnchiridionsApiService();

	NewPostActionApiService getNewPostActionApiService();
}
