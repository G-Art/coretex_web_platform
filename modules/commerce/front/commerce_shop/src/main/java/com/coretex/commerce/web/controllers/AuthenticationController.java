package com.coretex.commerce.web.controllers;

import com.coretex.commerce.config.security.AuthRequest;
import com.coretex.commerce.config.security.AuthResponse;
import com.coretex.commerce.config.security.JWTUtil;
import com.coretex.commerce.config.security.PBKDF2Encoder;
import com.coretex.commerce.config.security.service.JWTUserService;
import com.coretex.commerce.data.requests.RegisterRequest;
import com.coretex.commerce.facades.CustomerFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Date;

import static org.apache.http.client.utils.URIUtils.extractHost;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController()
@RequestMapping("/v1")
public class AuthenticationController {

	@Resource
	private JWTUtil jwtUtil;

	@Resource
	private PBKDF2Encoder passwordEncoder;

	@Resource
	private JWTUserService userRepository;

	@Resource
	private CustomerFacade customerFacade;

	@Value("${jwt.jjwt.expiration}")
	private String expirationTime;


	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public Mono<ResponseEntity<?>> logout(ServerWebExchange exchange) {
		return exchange.getSession()
				.flatMap(WebSession::invalidate)
				.flatMap( aVoid ->  Mono.subscriberContext()
						.map(context -> ReactiveSecurityContextHolder.clearContext()
								.apply(context)))
				.map(c -> ResponseEntity.status(HttpStatus.OK).build());
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar, ServerHttpRequest request) {
		return userRepository.findByUsername(ar.getName()).map((userDetails) -> {
			if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
				var generateToken = jwtUtil.generateToken(userDetails);
				return ResponseEntity.ok()
						.headers(generateCookieHeader(generateToken, request))
						.body(new AuthResponse(generateToken));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Mono<ResponseEntity<?>> register(@RequestBody RegisterRequest rr, ServerHttpRequest request) {
		rr.setEncoder(passwordEncoder::encode);
		var domain = extractHost(request.getURI()).getHostName();
		var register = customerFacade.register(rr, domain);
		if (!register.isHasErrors()) {
			var authRequest = new AuthRequest();
			authRequest.setName(rr.getEmail());
			authRequest.setPassword(rr.getPassword());
			return login(authRequest, request);
		}
		return Mono.fromSupplier(() -> ResponseEntity.status(HttpStatus.valueOf(register.getErrors().keySet().iterator().next())).body(register));

	}


	public HttpHeaders generateCookieHeader(String jwt, ServerHttpRequest request) {
		String cookieHeaderLine = getAccessCookieHeader(request, jwt);
		HttpHeaders headers = new HttpHeaders();
		headers.add(SET_COOKIE, cookieHeaderLine);
		return headers;
	}

	public String getAccessCookieHeader(ServerHttpRequest request, String jwt) {
		Date expiryDate = getExpiredTime();

		StringBuilder sb = new StringBuilder("accessToken=");
		sb.append(jwt)
				.append(";expires=").append(expiryDate)
				.append(";path=/");

		return sb.toString();
	}

	private Date getExpiredTime() {
		long expirationTimeLong = Long.parseLong(expirationTime);
		return new Date(System.currentTimeMillis() + expirationTimeLong * 1000);//in second
	}
}
