package com.coretex.commerce.web.controllers;

import com.coretex.commerce.config.security.service.JWTUserService;
import com.coretex.commerce.data.CustomerData;
import com.coretex.commerce.mapper.CustomerDataMapper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

@RestController()
@RequestMapping("/v1/user")
public class UserController {

	@Resource
	private JWTUserService userRepository;

	@Resource
	private CustomerDataMapper customerDataMapper;

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Mono<CustomerData> currentUser(ServerWebExchange exchange) {
		return userRepository.getCurrentUser(exchange)
				.map(customerDataMapper::fromItem);
	}

	@RequestMapping(value = "/language", method = RequestMethod.POST)
	public Mono<CustomerData> setDefaultLanguage(ServerWebExchange exchange, @RequestBody Map<String, String> requestParams) {
		return userRepository.getCurrentUser(exchange)
				.switchIfEmpty(Mono.error(new AuthenticationCredentialsNotFoundException("User not logged in")))
				.map(customerItem -> userRepository.setDefaultLanguage(customerItem, requestParams.get("lang")))
				.map(customerDataMapper::fromItem);
	}

}
