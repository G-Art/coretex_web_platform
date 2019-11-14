package com.coretex.core.business.services.order.orderproduct;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;


public interface OrderProductDownloadService extends SalesManagerEntityService<OrderProductDownloadItem> {

	/**
	 * List {@link OrderProductDownloadItem} by order id
	 *
	 * @param orderId
	 * @return
	 */
	List<OrderProductDownloadItem> getByOrderId(UUID orderId);

}
