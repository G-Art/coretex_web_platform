package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CartDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.CartCalculationService;
import com.coretex.commerce.core.services.CartService;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.enums.cx_core.OrderStatusEnum;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.cx_core.OrderEntryItem;
import com.coretex.items.cx_core.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultCartService extends AbstractGenericItemService<CartItem> implements CartService {

	private final static Logger LOG = LoggerFactory.getLogger(DefaultCartService.class);

	@Resource
	private CartCalculationService cartCalculationService;

	@Resource
	private ItemService itemService;

	public DefaultCartService(CartDao repository) {
		super(repository);
	}

	@Override
	public Stream<CartItem> getCartsForCustomer(CustomerItem customerItem) {
		return getRepository().getCartsForCustomer(customerItem);
	}

	@Override
	public void merge(CartItem mainCart, CartItem slaveCart) {
		slaveCart.getEntries()
				.forEach(orderEntryItem -> {
					mergeEntry(mainCart, orderEntryItem);
				});
		cartCalculationService.calculate(mainCart);
	}

	@Override
	public void placeOrder(CartItem cartItem) {

		OrderItem orderItem = new OrderItem();
		orderItem.setCustomer(cartItem.getCustomer());
		orderItem.setStatus(OrderStatusEnum.CREATED);
		orderItem.setDatePurchased(new Date());
		orderItem.setConfirmedAddress(false);
		orderItem.setAddress(cartItem.getAddress());
		orderItem.setDeliveryType(cartItem.getDeliveryType());
		orderItem.setPaymentMode(cartItem.getPaymentMode());
		orderItem.setCurrency(cartItem.getCurrency());
		orderItem.setPaymentInfo(cartItem.getPaymentInfo());
		orderItem.setStore(cartItem.getStore());
		orderItem.setLocale(orderItem.getLocale());
		orderItem.setTotal(cartItem.getTotal());
		orderItem.setPaymentTransactions(cartItem.getPaymentTransactions());

		cartItem.getEntries().forEach(orderEntryItem -> {
			OrderEntryItem orderE = new OrderEntryItem();
			orderE.setQuantity(orderEntryItem.getQuantity());
			orderE.setOrder(orderItem);
			orderE.setProduct(orderEntryItem.getProduct());
			orderE.setCalculated(orderEntryItem.getCalculated());
			orderE.setTotalPrice(orderEntryItem.getTotalPrice());
			orderE.setPrice(orderEntryItem.getPrice());
			orderItem.getEntries().add(orderE);
		});

		itemService.save(orderItem);
		itemService.delete(cartItem);
	}

	private void mergeEntry(CartItem mainCart, OrderEntryItem orderEntryItem) {
		var correspondsEntry = mainCart.getEntries()
				.stream()
				.filter(entry -> entry.getProduct()
						.getCode()
						.equals(orderEntryItem.getProduct()
								.getCode()
						)
				).collect(Collectors.toList());
		if(correspondsEntry.isEmpty()){
			mainCart.getEntries().add(orderEntryItem);
			orderEntryItem.setOrder(mainCart);
		}else {
			if(correspondsEntry.size() != 1){
				LOG.warn(String.format("Cart [%s] duplication entry for product [%s]", mainCart.getUuid(), orderEntryItem.getProduct().getCode()));
			}else {
				var orderEntry = correspondsEntry.get(0);
				orderEntry.setQuantity(orderEntry.getQuantity() + orderEntryItem.getQuantity());
			}
		}
	}

	@Override
	public CartDao getRepository() {
		return (CartDao) super.getRepository();
	}
}
