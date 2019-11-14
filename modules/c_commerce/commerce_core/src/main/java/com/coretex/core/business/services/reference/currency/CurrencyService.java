package com.coretex.core.business.services.reference.currency;

import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CurrencyItem;

public interface CurrencyService extends SalesManagerEntityService<CurrencyItem> {

	CurrencyItem getByCode(String code);

}
