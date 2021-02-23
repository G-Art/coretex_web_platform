package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.CartService;
import com.coretex.commerce.core.services.CartValidationService;
import com.coretex.commerce.core.services.CustomerService;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.data.CartData;
import com.coretex.commerce.data.OrderPlaceResult;
import com.coretex.commerce.delivery.api.service.DeliveryServiceService;
import com.coretex.commerce.facades.CartFacade;
import com.coretex.commerce.mapper.CartDataMapper;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.payment.service.PaymentModeService;
import com.coretex.commerce.strategies.cart.AddToCartStrategy;
import com.coretex.commerce.strategies.cart.UpdateCartEntryStrategy;
import com.coretex.commerce.strategies.cart.impl.CartParameter;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.cx_core.VariantProductItem;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class DefaultCartFacade implements CartFacade {

	@Resource
	private CartService cartService;

	@Resource
	private DeliveryServiceService deliveryServiceService;

	@Resource
	private PaymentModeService paymentModeService;

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

	@Resource
	private CustomerService customerService;

	@Resource
	private ItemService itemService;

	@Resource
	private CartValidationService cartValidationService;

	@Override
	public CartData getByUUID(UUID uuid) {
		return cartDataMapper.fromItem(cartService.getByUUID(uuid));
	}

	@Override
	public CartData getOrCreateCart(UUID uuid, String storeDomain, CustomerItem customer) {
		CartItem cartItem = null;
		if (Objects.nonNull(uuid)) {
			cartItem = cartService.getByUUID(uuid);
			if (Objects.nonNull(cartItem) && Objects.isNull(cartItem.getCustomer()) && Objects.nonNull(customer)) {
				cartItem.setCustomer(customer);
				cartService.save(cartItem);
			}
		}

		if (Objects.isNull(cartItem)) {
			return createCart(storeDomain, customer);
		}
		return cartDataMapper.fromItem(cartItem);
	}

	@Override
	public CartData createCart(String storeDomain, CustomerItem customer) {
		var cartItem = new CartItem();
		cartItem.setCustomer(customer);
		var store = storeService.getByDomain(storeDomain);
		if (Objects.isNull(store)) {
			store = storeService.getByCode("DEFAULT");
		}
		cartItem.setStore(store);
		cartService.save(cartItem);
		return cartDataMapper.fromItem(cartItem);
	}

	@Override
	public Flux<CartData> getCartsForCustomer(UUID customerUuid) {
		return Optional.ofNullable(customerService.getByUUID(customerUuid))
				.map(cartService::getCartsForCustomer)
				.map(stream -> stream.map(cartDataMapper::fromItem)).orElseGet(Flux::empty);
	}

	@Override
	public CartData updateCart(CartData cartData, UUID entry, Integer quantity) {
		return executeForCart(cartData.getUuid(), cartItem -> {
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
			return cartItem;
		});
	}

	@Override
	public CartData addToCart(CartData cartData, UUID product, Integer quantity) {

		return executeForCart(cartData.getUuid(), cartItem -> {
			if (Objects.isNull(cartItem)) {
				cartItem = new CartItem();
			}

			CartParameter cartParameter = new CartParameter();
			cartParameter.setCart(cartItem);
			cartParameter.setProduct((VariantProductItem) productService.getByUUID(product));
			cartParameter.setQuantity(quantity);

			addToCartStrategy.addToCart(cartParameter);

			cartService.save(cartItem);
			return cartItem;
		});
	}

	@Override
	public CartData setDeliveryType(CartData cartData, UUID deliveryType) {

		return executeForCart(cartData.getUuid(), cartItem -> {
			if (Objects.isNull(cartItem)) {
				cartItem = new CartItem();
			}

			if (Objects.nonNull(deliveryType)) {
				var deliveryTypeItem = deliveryServiceService.getDeliveryTypeByUUID(deliveryType);

				cartItem.setDeliveryType(deliveryTypeItem);
			} else {
				cartItem.setDeliveryType(null);
			}

			cartService.save(cartItem);
			return cartItem;
		});
	}

	@Override
	public CartData saveDeliveryInfo(CartData cartData, Map<String, Object> info) {
		return executeForCart(cartData.getUuid(), cartItem -> {
			deliveryServiceService.saveDeliveryInfo(cartItem, info);
		});
	}

	@Override
	public CartData merge(CartData sessionCart, CartData userCart) {
		var mainCart = cartService.getByUUID(sessionCart.getUuid());
		var cart = cartService.getByUUID(userCart.getUuid());
		if (Objects.nonNull(mainCart) && Objects.nonNull(cart)) {
			cartService.merge(mainCart, cart);
			itemService.save(mainCart);
			itemService.delete(cart);
		}
		cartDataMapper.updateFromItem(mainCart, sessionCart);
		return sessionCart;
	}

	@Override
	public CartData setPaymentType(CartData cartData, UUID paymentMode) {
		return executeForCart(cartData.getUuid(), cartItem -> {
			if (Objects.isNull(cartItem)) {
				cartItem = new CartItem();
			}

			if (Objects.nonNull(paymentMode)) {
				var deliveryTypeItem = paymentModeService.getByUUID(paymentMode);

				cartItem.setPaymentMode(deliveryTypeItem);
			} else {
				cartItem.setPaymentMode(null);
			}

			cartService.save(cartItem);
			return cartItem;
		});
	}

	@Override
	public OrderPlaceResult palaceOrder(CartData cartData) {
		var cartItem = cartService.getByUUID(cartData.getUuid());

		var validationResult = cartValidationService.validate(cartItem);

		OrderPlaceResult orderPlaceResult = new OrderPlaceResult();

		orderPlaceResult.setAddressValid(true);
		orderPlaceResult.setPaymentValid(true);
		orderPlaceResult.setDeliveryTypeValid(true);
		orderPlaceResult.setOrderCreated(true);

		cartService.placeOrder(cartItem);

		return orderPlaceResult;
	}

	@Override
	public void delete(UUID uuid) {
		cartService.delete(itemService.create(CartItem.class, uuid));
	}

	private CartData executeForCart(UUID cart, Function<CartItem, CartItem> cartItemFunction) {
		var cartItem = cartService.getByUUID(cart);
		return cartDataMapper.fromItem(cartItemFunction.apply(cartItem));
	}

	private CartData executeForCart(UUID cart, Consumer<CartItem> cartItemConsumer) {
		var cartItem = cartService.getByUUID(cart);
		cartItemConsumer.accept(cartItem);
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
