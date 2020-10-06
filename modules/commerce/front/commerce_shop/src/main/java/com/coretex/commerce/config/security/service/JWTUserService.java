package com.coretex.commerce.config.security.service;

import com.coretex.commerce.config.security.user.JWTRole;
import com.coretex.commerce.config.security.user.JWTUser;
import com.coretex.commerce.core.services.CustomerService;
import com.coretex.commerce.core.services.LocaleService;
import com.coretex.items.cx_core.CustomerItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Service
public class JWTUserService {

	@Resource
	private CustomerService customerService;
	@Resource
	private LocaleService localeService;


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

	public Mono<CustomerItem> getCurrentUser(ServerWebExchange exchange) {
		return ReactiveSecurityContextHolder.getContext()
				.map(SecurityContext::getAuthentication)
				.map(Authentication::getPrincipal)
				.cast(String.class)
				.flatMap(email -> {
					var customer = customerService.getByEmail(email);
					if (Objects.nonNull(customer)) {
						return Mono.just(customer);
					}
					return Mono.empty();
				});
	}

	public CustomerItem setDefaultLanguage(CustomerItem customerItem, String lang) {
		if (StringUtils.isNotBlank(lang) &&
				(Objects.isNull(customerItem.getLanguage()) ||
				!containsIgnoreCase(customerItem.getLanguage().getIso(), lang))) {
			var locale = localeService.getByIso(lang);
			customerItem.setLanguage(locale);
			customerService.save(customerItem);
		}
		return customerItem;
	}
}
