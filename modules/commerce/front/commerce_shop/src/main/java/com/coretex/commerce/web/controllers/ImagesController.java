package com.coretex.commerce.web.controllers;

import com.coretex.commerce.core.dto.FileContentType;
import com.coretex.commerce.core.manager.OutputContentFile;
import com.coretex.commerce.core.manager.ProductImageSize;
import com.coretex.commerce.core.services.ProductImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1")
public class ImagesController {

	@Resource
	private ProductImageService productImageService;

	@RequestMapping("/static/products/{storeCode}/{productCode}/{imageSize}/{imageName}.{extension}")
	public Mono<ResponseEntity<ByteArrayResource>> printImage(@PathVariable final String storeCode,
															  @PathVariable final String productCode,
															  @PathVariable final String imageSize,
															  @PathVariable final String imageName,
															  @PathVariable final String extension) {

		// product image small
		// example small product image -> /static/products/DEFAULT/TB12345/SMALL/product1.jpg

		// example large product image -> /static/products/DEFAULT/TB12345/LARGE/product1.jpg

		ProductImageSize size = ProductImageSize.valueOf(imageSize);

		if (FileContentType.PRODUCTLG.name().equals(imageSize)) {
			size = ProductImageSize.LARGE;
		}


		OutputContentFile image = null;
		image = productImageService.getProductImage(storeCode, productCode, imageName + "." + extension, size);
		if (image != null) {

			byte[] i = image.getFile().toByteArray();

			return Mono.just(ResponseEntity
					.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION)
					.body(new ByteArrayResource(i)));
		} else {
			//empty image placeholder
			return Mono.just(ResponseEntity.notFound().build());
		}

	}
}
