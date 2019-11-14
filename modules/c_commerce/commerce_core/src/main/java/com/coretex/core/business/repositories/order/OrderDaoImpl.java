package com.coretex.core.business.repositories.order;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.core.model.common.CriteriaOrderBy;
import com.coretex.core.model.order.OrderCriteria;
import com.coretex.core.model.order.OrderList;
import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderDaoImpl extends DefaultGenericDao<OrderItem> implements OrderDao {

	public OrderDaoImpl() {
		super(OrderItem.ITEM_TYPE);
	}

	@Override
	public OrderItem findOne(UUID id) {
		return find(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public OrderList listByStore(MerchantStoreItem store, OrderCriteria criteria) {


		StringBuilder query = new StringBuilder();
		query.append("SELECT o.* FROM \"" + OrderItem.ITEM_TYPE + "\" AS o ");

		if (!StringUtils.isBlank(criteria.getCustomerName())) {
			query.append("LEFT JOIN "+ BillingItem.ITEM_TYPE + " AS bill ON (o.billing = bill.uuid) ") ;
		}

		query.append("WHERE o.merchant=:mId ");


		if (!StringUtils.isBlank(criteria.getCustomerName())) {
			query.append("AND (bill.firstName like :nm OR bill.lastName like :nm) ");
		}

		if (!StringUtils.isBlank(criteria.getPaymentMethod())) {
			query.append("AND o.paymentModuleCode like :pm ");
		}

		if (criteria.getCustomerId() != null) {
			query.append("AND o.customerId = :cid ");
		}

		String orderByCriteria = " ORDER BY o.updateDate desc";

		if (criteria.getOrderBy() != null) {
			if (CriteriaOrderBy.ASC.name().equals(criteria.getOrderBy().name())) {
				orderByCriteria = " ORDER BY o.updateDate asc";
			}
		}

		query.append(orderByCriteria);


		OrderList orderList = new OrderList();

		Map<String, Object> params = Maps.newHashMap();

		params.put("mId", store);


		if(!StringUtils.isBlank(criteria.getCustomerName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getCustomerName()).append("%").toString();
			params.put("nm",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getPaymentMethod())) {
			String payementParam = new StringBuilder().append("%").append(criteria.getPaymentMethod()).append("%").toString();
			params.put("pm",payementParam);
		}

		if(criteria.getCustomerId()!=null) {
			params.put("cid", criteria.getCustomerId());
		}


//		Number count = (Number) countQ.getSingleResult();
//
//		orderList.setTotalCount(count.intValue());

//        if(count.intValue()==0)
//        	return orderList;

		//TO BE USED
		int max = criteria.getMaxCount();
		int first = criteria.getStartIndex();

//        objectQ.setFirstResult(first);


//    	if(max>0) {
//    			int maxCount = first + max;
//
//    			if(maxCount < count.intValue()) {
//    				objectQ.setMaxResults(maxCount);
//    			} else {
//    				objectQ.setMaxResults(count.intValue());
//    			}
//    	}
//

		var result = getSearchService().<OrderItem>search(query.toString(), params);
		orderList.setOrders(result.getResult());
		return orderList;


	}

	@Override
	public OrderList getOrders(OrderCriteria criteria) {
		OrderList orderList = new OrderList();

		StringBuilder query = new StringBuilder();
		query.append("SELECT o.* FROM \"" + OrderItem.ITEM_TYPE + "\" AS o ");

		String orderByCriteria = " ORDER BY o.updateDate desc";

		if (criteria.getOrderBy() != null) {
			if (CriteriaOrderBy.ASC.name().equals(criteria.getOrderBy().name())) {
				orderByCriteria = " ORDER BY o.updateDate asc";
			}
		}
		query.append(orderByCriteria);

		var result = getSearchService().<OrderItem>search(query.toString());
		orderList.setOrders(result.getResult());
		return orderList;

	}

}
