package com.coretex.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/system")
public class SystemController extends AbstractController {

	@RequestMapping(method = RequestMethod.GET)
	public String system() {
		return "/server/system";
	}

}
