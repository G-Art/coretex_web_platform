
package com.coretex.core.business.utils;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


public interface EntityPopulator<Source, Target> {

	Target populateToEntity(Source source, Target target, MerchantStoreItem store) throws ConversionException;

	Target populateToEntity(Source source) throws ConversionException;
}
