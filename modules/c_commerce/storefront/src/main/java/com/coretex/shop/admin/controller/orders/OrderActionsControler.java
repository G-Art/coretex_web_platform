package com.coretex.shop.admin.controller.orders;

import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.data.orders.Refund;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.EmailTemplatesUtils;
import com.coretex.shop.utils.LabelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Manage order details
 *
 * @author Carl Samson
 */
@Controller
public class OrderActionsControler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderActionsControler.class);

	@Resource
	private LabelUtils messages;

	@Resource
	private OrderService orderService;

	@Resource
	EmailTemplatesUtils emailTemplatesUtils;


	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/captureOrder.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> captureOrder(HttpServletRequest request, HttpServletResponse response, Locale locale) {


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		String sId = request.getParameter("id");

		AjaxResponse resp = new AjaxResponse();

		try {
			UUID id = UUID.fromString(sId);

			OrderItem order = orderService.getByUUID(id);

			if (order == null) {

				LOGGER.error("OrderItem {0} does not exists", id);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!order.getMerchant().getUuid().equals(store.getUuid())) {

				LOGGER.error("Merchant store does not have order {0}", id);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/refundOrder.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> refundOrder(@RequestBody Refund refund, HttpServletRequest request, HttpServletResponse response, Locale locale) {


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		AjaxResponse resp = new AjaxResponse();

		BigDecimal submitedAmount = null;

		try {

			OrderItem order = orderService.getByUUID(refund.getOrderId());

			if (order == null) {

				LOGGER.error("OrderItem {0} does not exists", refund.getOrderId());
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!order.getMerchant().getUuid().equals(store.getUuid())) {

				LOGGER.error("Merchant store does not have order {0}", refund.getOrderId());
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			//parse amount
			try {
				submitedAmount = new BigDecimal(refund.getAmount());
				if (submitedAmount.doubleValue() == 0) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resp.setStatusMessage(messages.getMessage("message.invalid.amount", locale));
					String returnString = resp.toJSONString();
					return new ResponseEntity<String>(returnString, HttpStatus.OK);
				}

			} catch (Exception e) {
				LOGGER.equals("invalid refundAmount " + refund.getAmount());
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			BigDecimal orderTotal = order.getTotal();
			if (submitedAmount.doubleValue() > orderTotal.doubleValue()) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setStatusMessage(messages.getMessage("message.invalid.amount", locale));
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (submitedAmount.doubleValue() <= 0) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setStatusMessage(messages.getMessage("message.invalid.amount", locale));
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch (Exception e) {
			LOGGER.error("Error while processing refund", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/printInvoice.html", method = RequestMethod.GET)
	public void printInvoice(HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		String sId = request.getParameter("id");

		try {

			UUID id = UUID.fromString(sId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
			OrderItem order = orderService.getByUUID(id);

			if (!order.getMerchant().getUuid().equals(store.getUuid())) {
				throw new Exception("Invalid order");
			}


			LocaleItem lang = store.getDefaultLanguage();


			ByteArrayOutputStream stream = orderService.generateInvoice(store, order, lang);
			StringBuilder attachment = new StringBuilder();
			//attachment.append("attachment; filename=");
			attachment.append(order.getUuid());
			attachment.append(".pdf");

			response.setHeader("ContentItem-disposition", "attachment;filename=" + attachment.toString());

			//Set the mime type for the response
			response.setContentType("application/pdf");


			response.getOutputStream().write(stream.toByteArray());

			response.flushBuffer();


		} catch (Exception e) {
			LOGGER.error("Error while printing a report", e);
		}


	}


	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/sendInvoice.html", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> sendInvoice(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sId = request.getParameter("id");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		AjaxResponse resp = new AjaxResponse();

		if (sId == null) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}


		try {

			UUID id = UUID.fromString(sId);


			OrderItem dbOrder = orderService.getByUUID(id);

			if (dbOrder == null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			if (!dbOrder.getMerchant().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Cannot get transactions for order id " + sId, e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}


	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/updateStatus.html", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sId = request.getParameter("id");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		AjaxResponse resp = new AjaxResponse();

		if (sId == null) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}


		try {

			UUID id = UUID.fromString(sId);


			OrderItem dbOrder = orderService.getByUUID(id);

			if (dbOrder == null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			if (!dbOrder.getMerchant().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			Set<OrderStatusHistoryItem> orderStatus = dbOrder.getOrderHistory();
			OrderStatusHistoryItem lastHistory = null;
			if (orderStatus != null) {
				int count = 1;
				for (OrderStatusHistoryItem history : orderStatus) {
					if (count == orderStatus.size()) {
						lastHistory = history;
						break;
					}
					count++;
				}
			}

			if (lastHistory == null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("No history");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Cannot get transactions for order id " + sId, e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(e.getMessage());
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/orders/sendDownloadEmail.html", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<String> sendDownloadEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sId = request.getParameter("id");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		AjaxResponse resp = new AjaxResponse();

		if (sId == null) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}


		try {

			UUID id = UUID.fromString(sId);


			OrderItem dbOrder = orderService.getByUUID(id);

			if (dbOrder == null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			if (!dbOrder.getMerchant().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Cannot get transactions for order id " + sId, e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(e.getMessage());
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}


}
