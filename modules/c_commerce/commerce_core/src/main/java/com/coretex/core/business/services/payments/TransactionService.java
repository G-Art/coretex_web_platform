package com.coretex.core.business.services.payments;

import java.util.Date;
import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.TransactionItem;


public interface TransactionService extends SalesManagerEntityService<TransactionItem> {

	/**
	 * Obtain a previous transaction that has type authorize for a give order
	 *
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	TransactionItem getCapturableTransaction(OrderItem order) throws ServiceException;

	TransactionItem getRefundableTransaction(OrderItem order) throws ServiceException;

	List<TransactionItem> listTransactions(OrderItem order) throws ServiceException;

	List<TransactionItem> listTransactions(Date startDate, Date endDate) throws ServiceException;


}