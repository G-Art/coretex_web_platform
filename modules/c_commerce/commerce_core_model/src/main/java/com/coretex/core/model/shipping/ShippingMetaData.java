package com.coretex.core.model.shipping;

import java.util.List;

import com.coretex.items.core.CountryItem;

/**
 * Describes how shipping is configured for a given store
 *
 * @author carlsamson
 */
public class ShippingMetaData {

	private List<String> modules;
	private List<String> preProcessors;
	private List<String> postProcessors;
	private List<CountryItem> shipToCountry;
	private boolean useDistanceModule;


	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public List<String> getPreProcessors() {
		return preProcessors;
	}

	public void setPreProcessors(List<String> preProcessors) {
		this.preProcessors = preProcessors;
	}

	public List<String> getPostProcessors() {
		return postProcessors;
	}

	public void setPostProcessors(List<String> postProcessors) {
		this.postProcessors = postProcessors;
	}

	public List<CountryItem> getShipToCountry() {
		return shipToCountry;
	}

	public void setShipToCountry(List<CountryItem> shipToCountry) {
		this.shipToCountry = shipToCountry;
	}

	public boolean isUseDistanceModule() {
		return useDistanceModule;
	}

	public void setUseDistanceModule(boolean useDistanceModule) {
		this.useDistanceModule = useDistanceModule;
	}

}
