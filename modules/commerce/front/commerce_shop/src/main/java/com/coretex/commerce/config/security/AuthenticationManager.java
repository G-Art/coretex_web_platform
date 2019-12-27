package com.coretex.commerce.config.security;

import com.coretex.commerce.config.security.user.JWTRole;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Resource
	private JWTUtil jwtUtil;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();

		String username;
		try {
			username = jwtUtil.getUsernameFromToken(authToken);
		} catch (Exception e) {
			username = null;
		}
		if (username != null && jwtUtil.validateToken(authToken)) {
			Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
			List<String> rolesMap = claims.get("role", List.class);
			List<JWTRole> roles = new ArrayList<>();
			for (String rolemap : rolesMap) {
				roles.add(JWTRole.valueOf(rolemap));
			}
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					username,
					null,
					roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
			);
			return Mono.just(auth);
		} else {
			return Mono.empty();
		}
	}
}