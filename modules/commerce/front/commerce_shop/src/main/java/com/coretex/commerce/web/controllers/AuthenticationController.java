package com.coretex.commerce.web.controllers;

import com.coretex.commerce.config.security.AuthRequest;
import com.coretex.commerce.config.security.AuthResponse;
import com.coretex.commerce.config.security.JWTUtil;
import com.coretex.commerce.config.security.PBKDF2Encoder;
import com.coretex.commerce.config.security.service.JWTUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Date;

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

	@Value("${jwt.jjwt.expiration}")
	private String expirationTime;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar, ServerHttpRequest request) {
		return userRepository.findByUsername(ar.getName()).map((userDetails) -> {
			if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
				var generateToken = jwtUtil.generateToken(userDetails);
				return ResponseEntity.ok().headers(generateCookieHeader(generateToken, request)).body(new AuthResponse(generateToken));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
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
				.append(";path=/")
				.append(";HttpOnly");

//		if (request.isSecure()) {
//			sb.append(";Secure");
//		}
		return sb.toString();
	}

	private Date getExpiredTime() {
		long expirationTimeLong = Long.parseLong(expirationTime);
		return new Date(System.currentTimeMillis() + expirationTimeLong * 1000);//in second
	}
}
