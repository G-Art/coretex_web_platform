package com.coretex.newpost.api.impl;

import com.coretex.newpost.api.NewPostApiService;
import com.coretex.newpost.api.actions.NewPostActionApiService;
import com.coretex.newpost.api.address.NewPostAddressApiService;
import com.coretex.newpost.api.enchiridion.NewPostEnchiridionsApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DefaultNewPostApiService implements NewPostApiService {

	@Resource
	private NewPostAddressApiService newPostAddressApiService;

	@Resource
	private NewPostEnchiridionsApiService newPostEnchiridionsApiService;

	@Resource
	private NewPostActionApiService newPostActionApiService;

	@Override
	public NewPostAddressApiService getNewPostAddressApiService(){
		return newPostAddressApiService;
	}

	@Override
	public NewPostEnchiridionsApiService getNewPostEnchiridionsApiService() {
		return newPostEnchiridionsApiService;
	}

	@Override
	public NewPostActionApiService getNewPostActionApiService() {
		return newPostActionApiService;
	}
}
