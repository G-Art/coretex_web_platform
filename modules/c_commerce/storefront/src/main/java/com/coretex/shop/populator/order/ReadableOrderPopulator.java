package com.coretex.shop.populator.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.enums.commerce_core_model.OrderTotalTypeEnum;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.customer.ReadableDelivery;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.model.order.ReadableOrder;

public class ReadableOrderPopulator extends
		AbstractDataPopulator<OrderItem, ReadableOrder> {

	@Override
	public ReadableOrder populate(OrderItem source, ReadableOrder target,
								  MerchantStoreItem store, LocaleItem language) throws ConversionException {


		target.setUuid(source.getUuid());
		target.setDatePurchased(source.getDatePurchased());
		target.setOrderStatus(source.getStatus());
		target.setCurrency(source.getCurrency().getCode());
		target.setCurrencyModel(source.getCurrency());


		if (source.getCustomerAgreement() != null) {
			target.setCustomerAgreed(source.getCustomerAgreement());
		}
		if (source.getConfirmedAddress() != null) {
			target.setConfirmedAddress(source.getConfirmedAddress());
		}

		com.coretex.shop.model.order.total.OrderTotal taxTotal = null;
		com.coretex.shop.model.order.total.OrderTotal shippingTotal = null;


		if (source.getBilling() != null) {
			Address address = new Address();
			address.setCity(source.getBilling().getCity());
			address.setAddress(source.getBilling().getAddressLine1());
			address.setFirstName(source.getBilling().getFirstName());
			address.setLastName(source.getBilling().getLastName());
			address.setPostalCode(source.getBilling().getPostalCode());
			address.setPhone(source.getBilling().getPhone());
			if (source.getBilling().getCountry() != null) {
				address.setCountry(source.getBilling().getCountry().getIsoCode());
			}
			if (source.getBilling().getZone() != null) {
				address.setZone(source.getBilling().getZone().getCode());
			}

			target.setBilling(address);
		}

		if (source.getDelivery() != null) {
			ReadableDelivery address = new ReadableDelivery();
			address.setCity(source.getDelivery().getCity());
			address.setAddress(source.getDelivery().getAddressLine1());
			address.setFirstName(source.getDelivery().getFirstName());
			address.setLastName(source.getDelivery().getLastName());
			address.setPostalCode(source.getDelivery().getPostalCode());
			address.setPhone(source.getDelivery().getPhone());
			if (source.getDelivery().getCountry() != null) {
				address.setCountry(source.getDelivery().getCountry().getIsoCode());
			}
			if (source.getDelivery().getZone() != null) {
				address.setZone(source.getDelivery().getZone().getCode());
			}

			target.setDelivery(address);
		}

		List<com.coretex.shop.model.order.total.OrderTotal> totals = new ArrayList<com.coretex.shop.model.order.total.OrderTotal>();
		for (OrderTotalItem t : source.getOrderTotal()) {
			if (t.getOrderTotalType() == null) {
				continue;
			}
			if (t.getOrderTotalType().name().equals(OrderTotalTypeEnum.TOTAL.name())) {
				com.coretex.shop.model.order.total.OrderTotal totalTotal = createTotal(t);
				target.setTotal(totalTotal);
				totals.add(totalTotal);
			} else if (t.getOrderTotalType().name().equals(OrderTotalTypeEnum.TAX.name())) {
				com.coretex.shop.model.order.total.OrderTotal totalTotal = createTotal(t);
				if (taxTotal == null) {
					taxTotal = totalTotal;
				} else {
					BigDecimal v = taxTotal.getValue();
					v = v.add(totalTotal.getValue());
					taxTotal.setValue(v);
				}
				target.setTax(totalTotal);
				totals.add(totalTotal);
			} else if (t.getOrderTotalType().name().equals(OrderTotalTypeEnum.SHIPPING.name())) {
				com.coretex.shop.model.order.total.OrderTotal totalTotal = createTotal(t);
				if (shippingTotal == null) {
					shippingTotal = totalTotal;
				} else {
					BigDecimal v = shippingTotal.getValue();
					v = v.add(totalTotal.getValue());
					shippingTotal.setValue(v);
				}
				target.setShipping(totalTotal);
				totals.add(totalTotal);
			} else if (t.getOrderTotalType().name().equals(OrderTotalTypeEnum.HANDLING.name())) {
				com.coretex.shop.model.order.total.OrderTotal totalTotal = createTotal(t);
				if (shippingTotal == null) {
					shippingTotal = totalTotal;
				} else {
					BigDecimal v = shippingTotal.getValue();
					v = v.add(totalTotal.getValue());
					shippingTotal.setValue(v);
				}
				target.setShipping(totalTotal);
				totals.add(totalTotal);
			} else if (t.getOrderTotalType().name().equals(OrderTotalTypeEnum.SUBTOTAL.name())) {
				com.coretex.shop.model.order.total.OrderTotal subTotal = createTotal(t);
				totals.add(subTotal);

			} else {
				com.coretex.shop.model.order.total.OrderTotal otherTotal = createTotal(t);
				totals.add(otherTotal);
			}
		}

		target.setTotals(totals);

		return target;
	}

	private com.coretex.shop.model.order.total.OrderTotal createTotal(OrderTotalItem t) {
		com.coretex.shop.model.order.total.OrderTotal totalTotal = new com.coretex.shop.model.order.total.OrderTotal();
		totalTotal.setCode(t.getOrderTotalCode());
		totalTotal.setUuid(t.getUuid());
		totalTotal.setOrder(t.getSortOrder());
		totalTotal.setValue(t.getValue());
		return totalTotal;
	}

	@Override
	protected ReadableOrder createTarget() {

		return null;
	}

}
