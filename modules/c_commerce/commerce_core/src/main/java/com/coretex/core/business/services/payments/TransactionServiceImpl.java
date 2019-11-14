package com.coretex.core.business.services.payments;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coretex.enums.commerce_core_model.TransactionTypeEnum;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.TransactionItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.payments.TransactionDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;


@Service("transactionService")
public class TransactionServiceImpl extends SalesManagerEntityServiceImpl<TransactionItem> implements TransactionService {


	private TransactionDao transactionDao;

	public TransactionServiceImpl(TransactionDao transactionDao) {
		super(transactionDao);
		this.transactionDao = transactionDao;
	}

	@Override
	public void create(TransactionItem transaction) {

		//parse JSON string
//		String transactionDetails = transaction.toJSONString();
//		if(!StringUtils.isBlank(transactionDetails)) {
//			transaction.setDetails(transactionDetails);
//		}
//
		super.create(transaction);


	}

	@Override
	public List<TransactionItem> listTransactions(OrderItem order) throws ServiceException {

		return transactionDao.findByOrder(order.getUuid());
	}

	@Override
	public TransactionItem getCapturableTransaction(OrderItem order)
			throws ServiceException {
		List<TransactionItem> transactions = transactionDao.findByOrder(order.getUuid());
		ObjectMapper mapper = new ObjectMapper();
		TransactionItem capturable = null;
		for (TransactionItem transaction : transactions) {
			if (transaction.getTransactionType().name().equals(TransactionTypeEnum.AUTHORIZE.name())) {
				if (!StringUtils.isBlank(transaction.getDetails())) {
					try {
						@SuppressWarnings("unchecked")
						Map<String, String> objects = mapper.readValue(transaction.getDetails(), Map.class);
//						transaction.setTransactionDetails(objects);
						capturable = transaction;
					} catch (Exception e) {
						throw new ServiceException(e);
					}
				}
			}
			if (transaction.getTransactionType().name().equals(TransactionTypeEnum.CAPTURE.name())) {
				break;
			}
			if (transaction.getTransactionType().name().equals(TransactionTypeEnum.REFUND.name())) {
				break;
			}
		}

		return capturable;
	}

	@Override
	public TransactionItem getRefundableTransaction(OrderItem order)
			throws ServiceException {
		List<TransactionItem> transactions = transactionDao.findByOrder(order.getUuid());
		Map<String, TransactionItem> finalTransactions = new HashMap<String, TransactionItem>();
		TransactionItem finalTransaction = null;
		for (TransactionItem transaction : transactions) {
			//System.out.println("TransactionItem type " + transaction.getTransactionType().name());
			if (transaction.getTransactionType().name().equals(TransactionTypeEnum.AUTHORIZECAPTURE.name())) {
				finalTransactions.put(TransactionTypeEnum.AUTHORIZECAPTURE.name(), transaction);
				continue;
			}
			if (transaction.getTransactionType().name().equals(TransactionTypeEnum.CAPTURE.name())) {
				finalTransactions.put(TransactionTypeEnum.CAPTURE.name(), transaction);
				continue;
			}
			if (transaction.getTransactionType().name().equals(TransactionTypeEnum.REFUND.name())) {
				//check transaction id
				TransactionItem previousRefund = finalTransactions.get(TransactionTypeEnum.REFUND.name());
				if (previousRefund != null) {
					Date previousDate = previousRefund.getTransactionDate();
					Date currentDate = transaction.getTransactionDate();
					if (previousDate.before(currentDate)) {
						finalTransactions.put(TransactionTypeEnum.REFUND.name(), transaction);
						continue;
					}
				} else {
					finalTransactions.put(TransactionTypeEnum.REFUND.name(), transaction);
					continue;
				}
			}
		}

		if (finalTransactions.containsKey(TransactionTypeEnum.AUTHORIZECAPTURE.name())) {
			finalTransaction = finalTransactions.get(TransactionTypeEnum.AUTHORIZECAPTURE.name());
		}

		if (finalTransactions.containsKey(TransactionTypeEnum.CAPTURE.name())) {
			finalTransaction = finalTransactions.get(TransactionTypeEnum.CAPTURE.name());
		}

		if (finalTransaction != null && !StringUtils.isBlank(finalTransaction.getDetails())) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String, String> objects = mapper.readValue(finalTransaction.getDetails(), Map.class);
//				finalTransaction.setTransactionDetails(objects);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		return finalTransaction;
	}

	@Override
	public List<TransactionItem> listTransactions(Date startDate, Date endDate) throws ServiceException {

		return transactionDao.findByDates(startDate, endDate);
	}

}
