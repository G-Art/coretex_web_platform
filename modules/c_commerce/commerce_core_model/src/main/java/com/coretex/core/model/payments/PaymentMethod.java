package com.coretex.core.model.payments;

import java.io.Serializable;

import com.coretex.core.model.system.IntegrationConfiguration;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.items.commerce_core_model.IntegrationModuleItem;

/**
 * Object to be used in store front with meta data and configuration
 * informations required to display to the end user
 *
 * @author Carl Samson
 */
public class PaymentMethod implements Serializable {


	private static final long serialVersionUID = 1L;
	private String paymentMethodCode;
	private PaymentTypeEnum paymentType;
	private boolean defaultSelected;
	private IntegrationModuleItem module;
	private IntegrationConfiguration informations;

	public PaymentTypeEnum getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentTypeEnum paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}

	public boolean isDefaultSelected() {
		return defaultSelected;
	}

	public void setDefaultSelected(boolean defaultSelected) {
		this.defaultSelected = defaultSelected;
	}

	public IntegrationModuleItem getModule() {
		return module;
	}

	public void setModule(IntegrationModuleItem module) {
		this.module = module;
	}

	public IntegrationConfiguration getInformations() {
		return informations;
	}

	public void setInformations(IntegrationConfiguration informations) {
		this.informations = informations;
	}

}
