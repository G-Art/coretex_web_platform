package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.admin.controllers.AbstractController;
import com.coretex.commerce.core.services.ProductImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
@RequestMapping("/image")
public class ImageController extends AbstractController {

	@Resource
	private ProductImageService productImageService;

	@RequestMapping(path = {"/remove/{uuid}"}, method = {RequestMethod.GET, RequestMethod.DELETE})
	public String removeProduct(@RequestHeader(value = "referer", required = false) final String referer,
								@PathVariable(value = "uuid") UUID uuid,
								RedirectAttributes redirectAttributes) {
		var image = productImageService.getByUUID(uuid);
		productImageService.delete(image);

		addInfoFlashMessage(redirectAttributes, "Image removed");

		return redirect(referer);
	}
}
