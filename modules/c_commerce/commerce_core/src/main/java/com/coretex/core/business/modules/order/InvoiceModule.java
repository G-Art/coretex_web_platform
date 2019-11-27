package com.coretex.core.business.modules.order;

import java.io.ByteArrayOutputStream;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.core.LocaleItem;


public interface InvoiceModule {

	ByteArrayOutputStream createInvoice(MerchantStoreItem store, OrderItem order, LocaleItem language) throws Exception;

}
