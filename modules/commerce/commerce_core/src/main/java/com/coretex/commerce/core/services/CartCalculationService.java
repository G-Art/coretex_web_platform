package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.AbstractOrderItem;

public interface CartCalculationService {

	void recalculate(AbstractOrderItem order);

	void calculate(AbstractOrderItem order);

	void calculate(AbstractOrderItem order, boolean force);
}
