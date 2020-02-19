package com.coretex.commerce.admin.controllers;

import com.coretex.commerce.core.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Controller
public class HomeController extends AbstractController {

	@Resource
	private OrderService orderService;

	@GetMapping(value = "/home")
	public String displayLogin(Model model) {

		LocalDate date = LocalDate.now();
		LocalDate firstDayOfYear = date.with(TemporalAdjusters.firstDayOfYear());
		var staticForPeriod = orderService.getStatisticForPeriod(Date.from(firstDayOfYear.atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant()));
		model.addAttribute("orderCount", staticForPeriod.get("count"));
		model.addAttribute("orderTotal", BigDecimal.valueOf((Double) staticForPeriod.get("sum")).setScale(2, RoundingMode.HALF_UP));

		return "home";
	}
}
