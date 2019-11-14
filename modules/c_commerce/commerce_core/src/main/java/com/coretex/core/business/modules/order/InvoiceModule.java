package com.coretex.core.business.modules.order;

import java.io.ByteArrayOutputStream;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.LanguageItem;


public interface InvoiceModule {

	ByteArrayOutputStream createInvoice(MerchantStoreItem store, OrderItem order, LanguageItem language) throws Exception;

}
