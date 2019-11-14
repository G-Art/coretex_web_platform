package com.coretex.shop.store.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("socialCustomerDetailsService")
public class SocialCustomerServicesImpl implements UserDetailsService {

	@Resource
	UserDetailsService customerDetailsService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//delegates to CustomerItem fetch service
		UserDetails userDetails = customerDetailsService.loadUserByUsername(username);

		return userDetails;
	}

}
