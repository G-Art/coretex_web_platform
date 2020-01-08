package com.coretex.commerce.config.security.service;

import com.coretex.commerce.config.security.user.JWTRole;
import com.coretex.commerce.config.security.user.JWTUser;
import com.coretex.commerce.core.services.CustomerService;
import com.coretex.items.cx_core.CustomerItem;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JWTUserService {

	@Resource
	private CustomerService customerService;

	public Mono<JWTUser> findByUsername(String username) {
		return Mono.fromSupplier(() -> customerService.getByEmail(username)).map(this::toJwtUser);
	}

	private JWTUser toJwtUser(CustomerItem userItem) {
		return new JWTUser(userItem.getUuid(),
				userItem.getFirstName(),
				userItem.getLastName(),
				userItem.getEmail(),
				userItem.getPassword(),
				List.of(JWTRole.ROLE_CUSTOMER),
				userItem.getActive(),
				LocalDateTime.now());
	}
}
