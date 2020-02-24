package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.services.CartCalculationService;
import com.coretex.items.cx_core.AbstractOrderItem;
import com.coretex.items.cx_core.OrderEntryItem;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Service
public class DefaultCartCalculationService implements CartCalculationService {

	@Override
	public void recalculate(AbstractOrderItem order) {
		calculate(order, true);
	}

	@Override
	public void calculate(AbstractOrderItem order) {
		calculate(order, false);
	}

	@Override
	public void calculate(AbstractOrderItem order, boolean force) {
		Assert.notNull(order, "Order must not be null");
		BigDecimal totalPrice = BigDecimal.ZERO;

		for(OrderEntryItem entry : order.getEntries()){
			calculateOrderEntry(entry, force);
			totalPrice = totalPrice.add(entry.getTotalPrice());

		}
		order.setTotal(totalPrice);

	}

	public void calculateOrderEntry(OrderEntryItem entry, boolean force) {
		if(force || calculationRequired(entry)){
			var quantity = entry.getQuantity();
			entry.setTotalPrice(entry.getPrice().multiply(BigDecimal.valueOf(quantity)));
			entry.setCalculated(true);
		}
	}

	private boolean calculationRequired(OrderEntryItem entry) {
		return !entry.getCalculated();
	}
}
