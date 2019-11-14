package com.coretex.shop.store.security.user;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class FacebookSocialUserDetailsService implements SocialUserDetailsService {


	@Resource
	UserDetailsService customerDetailsService;//delegate to current customer


	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookSocialUserDetailsService.class);


	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {


		CustomerDetails userDetails = (CustomerDetails) customerDetailsService.loadUserByUsername(userId);

		if (userDetails == null) {
			throw new UsernameNotFoundException("No user found with username: " + userId);
		}

		FacebookTokenUserDetails principal = new FacebookTokenUserDetails(
				userDetails.getUsername(),
				userDetails.getPassword(),
				userDetails.getAuthorities());

		principal.setFirstName(userDetails.getFirstName());
		principal.setId(String.valueOf(userDetails.getId()));
		principal.setLastName(userDetails.getLastName());

		LOGGER.debug("Found user details: {}", principal);

		return principal;

	}


}
