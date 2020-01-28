package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.ProductData;
import com.coretex.commerce.facades.ProductFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

	@Resource
	private ProductFacade productFacade;

	@GetMapping(path = {"/{code}"})
	private Mono<ProductData> getProductByCode(@PathVariable(value = "code") String code)
	{
		return Mono.just(productFacade.getByCode(code));
	}
}
