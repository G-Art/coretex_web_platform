
package com.coretex.core.populators;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;


public interface DataPopulator<Source, Target> {


	Target populate(Source source, Target target, MerchantStoreItem store, LanguageItem language) ;

	Target populate(Source source, MerchantStoreItem store, LanguageItem language);


}
