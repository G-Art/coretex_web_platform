
package com.coretex.core.populators;

import java.util.Locale;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;



public abstract class AbstractDataPopulator<Source, Target> implements DataPopulator<Source, Target> {


	private Locale locale;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}


	@Override
	public Target populate(Source source, MerchantStoreItem store, LanguageItem language) {
		return populate(source, createTarget(), store, language);
	}

	protected abstract Target createTarget();


}
