package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.CartService;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.data.CartData;
import com.coretex.commerce.facades.CartFacade;
import com.coretex.commerce.mapper.CartDataMapper;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.strategies.cart.AddToCartStrategy;
import com.coretex.commerce.strategies.cart.UpdateCartEntryStrategy;
import com.coretex.commerce.strategies.cart.impl.CartParameter;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.VariantProductItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

@Component
public class DefaultCartFacade implements CartFacade {

	@Resource
	private CartService cartService;

	@Resource
	private ProductService productService;

	@Resource
	private CartDataMapper cartDataMapper;

	@Resource
	private AddToCartStrategy addToCartStrategy;

	@Resource
	private UpdateCartEntryStrategy updateCartEntryStrategy;

	@Resource
	private StoreService storeService;

	@Override
	public CartData getByUUID(UUID uuid) {
		return cartDataMapper.fromItem(cartService.getByUUID(uuid));
	}

	@Override
	public CartData createCart(String storeDomain) {
		var cartItem = new CartItem();
		var store = storeService.getByDomain(storeDomain);
		if(Objects.isNull(store)){
			store = storeService.getByCode("DEFAULT");
		}
		cartItem.setStore(store);
		cartService.save(cartItem);
		return cartDataMapper.fromItem(cartItem);
	}

	@Override
	public CartData updateCart(CartData cartData, UUID entry, Integer quantity) {
		var cartItem = cartService.getByUUID(cartData.getUuid());

		if (Objects.nonNull(quantity)) {

			if (Objects.isNull(cartItem)) {
				cartItem = new CartItem();
			}

			CartParameter cartParameter = new CartParameter();
			cartParameter.setCart(cartItem);
			cartParameter.setEntryItem(cartItem.getEntries()
					.stream()
					.filter(entryItem -> entryItem.getUuid().equals(entry)).findAny()
					.orElse(null));
			cartParameter.setQuantity(quantity);

			updateCartEntryStrategy.updateCart(cartParameter);

			cartService.save(cartItem);
		}

		return cartDataMapper.fromItem(cartItem);
	}

	@Override
	public CartData addToCart(CartData cartData, UUID product, Integer quantity) {

		var cartItem = cartService.getByUUID(cartData.getUuid());

		if (Objects.isNull(cartItem)) {
			cartItem = new CartItem();
		}

		CartParameter cartParameter = new CartParameter();
		cartParameter.setCart(cartItem);
		cartParameter.setProduct((VariantProductItem) productService.getByUUID(product));
		cartParameter.setQuantity(quantity);

		addToCartStrategy.addToCart(cartParameter);

		cartService.save(cartItem);

		return cartDataMapper.fromItem(cartItem);
	}

	@Override
	public PageableService<CartItem> getPageableService() {
		return cartService;
	}

	@Override
	public GenericDataMapper<CartItem, CartData> getDataMapper() {
		return cartDataMapper;
	}

}
