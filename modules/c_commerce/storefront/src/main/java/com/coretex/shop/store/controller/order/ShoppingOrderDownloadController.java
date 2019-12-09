package com.coretex.shop.store.controller.order;

import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.model.content.FileContentType;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.store.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
@RequestMapping(Constants.SHOP_URI + "/order")
public class ShoppingOrderDownloadController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShoppingOrderDownloadController.class);

	@Resource
	private ContentService contentService;

	@Resource
	private OrderService orderService;


	/**
	 * Virtual product(s) download link
	 *
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping("/download/{orderId}/{id}.html")
	public @ResponseBody
	byte[] downloadFile(@PathVariable String orderId, @PathVariable String id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);


		FileContentType fileType = FileContentType.PRODUCT_DIGITAL;

		//get customer and check order
		OrderItem order = orderService.getByUUID(UUID.fromString(orderId));
		if (order == null) {
			LOGGER.warn("OrderItem is null for id " + orderId);
			response.sendError(404, "Image not found");
			return null;
		}


		//order belongs to customer
		CustomerItem customer = super.getSessionAttribute(Constants.CUSTOMER, request);
		if (customer == null) {
			response.sendError(404, "Image not found");
			return null;
		}




			return null;


	}


}
