package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.OrderEntryDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.OrderEntryService;
import com.coretex.commerce.core.utils.ProductUtils;
import com.coretex.items.cx_core.AbstractOrderItem;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.OrderEntryItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultOrderEntryService extends AbstractGenericItemService<OrderEntryItem> implements OrderEntryService {

	private OrderEntryDao repository;

	public DefaultOrderEntryService(OrderEntryDao repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public OrderEntryItem newOrderEntry(AbstractOrderItem abstractOrder) {
		var orderEntry = new OrderEntryItem();
		orderEntry.setOrder(abstractOrder);
		orderEntry.setQuantity(0);
		return orderEntry;
	}

	@Override
	public OrderEntryItem newOrderEntry(AbstractOrderItem abstractOrder, VariantProductItem product) {
		var orderEntry = newOrderEntry(abstractOrder);
		orderEntry.setProduct(product);
		var price = ProductUtils.getDefaultPrice(product);
		if (Objects.nonNull(price)) {
			orderEntry.setPrice(price.getProductPriceAmount());
		}
		return orderEntry;
	}


	@Override
	public OrderEntryItem newOrderEntry(AbstractOrderItem abstractOrder, VariantProductItem product, int quantity) {
		var orderEntry = newOrderEntry(abstractOrder, product);
		orderEntry.setQuantity(quantity);
		return orderEntry;
	}

	@Override
	public OrderEntryItem findOrCreateOrderEntry(CartItem cart, VariantProductItem product) {
		return cart.getEntries()
				.stream()
				.filter(orderEntryItem -> orderEntryItem.getProduct().getUuid().equals(product.getUuid()))
				.findFirst()
				.orElseGet(() -> newOrderEntry(cart, product));
	}

	@Override
	public void removeEntity(CartItem cart, ProductItem product) {
		Sets.newHashSet(cart.getEntries())
				.stream()
				.filter(orderEntryItem -> orderEntryItem.getProduct().getUuid().equals(product.getUuid()))
				.forEach(orderEntryItem -> {
					removeEntity(cart, orderEntryItem);
				});
	}

	@Override
	public void removeEntity(CartItem cart, OrderEntryItem entryItem) {
		cart.getEntries().remove(entryItem);
		repository.delete(entryItem);
	}
}
