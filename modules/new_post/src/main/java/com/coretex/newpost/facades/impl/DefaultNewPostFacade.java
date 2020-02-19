package com.coretex.newpost.facades.impl;

import com.coretex.newpost.api.NewPostApiService;
import com.coretex.newpost.dao.NewPostDeliveryTypeDao;
import com.coretex.newpost.facades.NewPostFacade;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DefaultNewPostFacade implements NewPostFacade {

	@Resource
	private NewPostDeliveryTypeDao newPostDeliveryTypeDao;

	@Resource
	private NewPostApiService newPostApiService;

//	@Override
//	public ShippingSummary calculateShippingSummary(String shippingMethod, String city, CartItem cart) {
//
//		var deliveryType = newPostDeliveryTypeDao.findByCode(shippingMethod);
//
//		if (Objects.nonNull(deliveryType)) {
//			var dateCalculation = requestDate(deliveryType, city, cart);
//			var priceCalculation = requestPrice(deliveryType, city, cart);
//			if (ArrayUtils.isNotEmpty(priceCalculation)) {
//				NewPostShippingSummary shippingSummary = new NewPostShippingSummary();
//				shippingSummary.setFreeShipping(false);
//				shippingSummary.setShipping(new BigDecimal(priceCalculation[0].getCost() + priceCalculation[0].getCostRedelivery()));
//				shippingSummary.setTaxOnShipping(false);
//				shippingSummary.setShippingOptionCode(shippingMethod);
//				if (ArrayUtils.isNotEmpty(dateCalculation)) {
//					shippingSummary.setDeliveryDate(StringUtils.substringBefore(dateCalculation[0].getDeliveryDate().getDate(), " "));
//				}
//
//				return shippingSummary;
//			}
//		}
//
//		return null;
//	}

//	private DocumentPriceValue[] requestPrice(NewPostDeliveryTypeItem deliveryType, String city, CartItem cart) {
//		var documentPriceProperties = new DocumentPriceProperties();
//		var deliveryService = (NewPostDeliveryServiceItem) deliveryType.getDeliveryService();
//		documentPriceProperties.setCityRecipient(city);
//		documentPriceProperties.setCitySender(deliveryService.getSenderCity().toString());
//		documentPriceProperties.setServiceType(String.format("%s%s", deliveryType.getSendFromWarehouse() ? "Warehouse" : "Doors", deliveryType.getSendToWarehouse() ? "Warehouse" : "Doors"));
//		documentPriceProperties.setCargoType("Parcel");
//		documentPriceProperties.setCost(cart.getSubTotal().setScale(0, RoundingMode.HALF_UP).intValue());
//		if (deliveryType.getPayOnDelivery()) {
//			var redeliveryCalculateProperties = new RedeliveryCalculateProperties();
//			redeliveryCalculateProperties.setCargoType("Money");
//			redeliveryCalculateProperties.setAmount(cart.getSubTotal().setScale(0, RoundingMode.HALF_UP).toBigInteger().toString());
//			documentPriceProperties.setRedeliveryCalculate(redeliveryCalculateProperties);
//		}
//		documentPriceProperties.setWeight("10");
//		documentPriceProperties.setSeatsAmount("1");
//		return newPostApiService.getNewPostActionApiService().getDocumentPrice(deliveryService, documentPriceProperties).getData();
//	}

//	private DocumentDeliveryDateValue[] requestDate(NewPostDeliveryTypeItem deliveryType, String city, CartItem cart) {
//		var documentDeliveryDateProperties = new DocumentDeliveryDateProperties();
//		var deliveryService = (NewPostDeliveryServiceItem) deliveryType.getDeliveryService();
//		documentDeliveryDateProperties.setCityRecipient(city);
//		documentDeliveryDateProperties.setCitySender(deliveryService.getSenderCity().toString());
//		documentDeliveryDateProperties.setServiceType(String.format("%s%s", deliveryType.getSendFromWarehouse() ? "Warehouse" : "Doors", deliveryType.getSendToWarehouse() ? "Warehouse" : "Doors"));
//		return newPostApiService.getNewPostActionApiService().getDocumentDeliveryDate(deliveryService, documentDeliveryDateProperties).getData();
//	}
}
