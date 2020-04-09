package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.LocaleData;
import com.coretex.commerce.facades.LocaleFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/v1/languages")
public class LanguageController {

	@Resource
	private LocaleFacade localeFacade;

	@GetMapping(path = "/store/{uuid}")
	private Flux<LocaleData> getLanguagesForStore(@PathVariable("uuid") UUID uuid) {
		return Flux.fromStream(() -> localeFacade.getByStore(uuid));
	}
}
