package com.coretex.core.business.repositories.payments;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.TransactionItem;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class TransactionDaoImpl extends DefaultGenericDao<TransactionItem> implements TransactionDao {

	public TransactionDaoImpl() {
		super(TransactionItem.ITEM_TYPE);
	}

	@Override
	public List<TransactionItem> findByOrder(UUID orderId) {
		String query = "SELECT t.* FROM \"" + TransactionItem.ITEM_TYPE + "\" AS t " +
				"WHERE t.\"order\" = :orderId";
		return getSearchService().<TransactionItem>search(query, Map.of("orderId", orderId)).getResult();
	}

	@Override
	public List<TransactionItem> findByDates(Date startDate, Date endDate) {
		String query = "SELECT t.* FROM " + TransactionItem.ITEM_TYPE + " AS t " +
				"WHERE t.transactionDate BETWEEN :from AND :to";
		return getSearchService().<TransactionItem>search(query, Map.of("from", startDate, "to", endDate)).getResult();
	}
}
