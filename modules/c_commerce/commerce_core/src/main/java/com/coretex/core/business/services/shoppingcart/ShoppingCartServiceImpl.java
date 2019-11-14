package com.coretex.core.business.services.shoppingcart;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.shoppingcart.ShoppingCartAttributeDao;
import com.coretex.core.business.repositories.shoppingcart.ShoppingCartItemDao;
import com.coretex.core.business.repositories.shoppingcart.ShoppingCartDao;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.shipping.ShippingProduct;
import com.coretex.items.commerce_core_model.ShoppingCartEntryAttributeItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.google.api.client.util.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends SalesManagerEntityServiceImpl<ShoppingCartItem>
		implements ShoppingCartService {

	private ShoppingCartDao shoppingCartDao;

	@Resource
	private ProductService productService;

	@Resource
	private ShoppingCartItemDao shoppingCartItemDao;

	@Resource
	private ShoppingCartAttributeDao shoppingCartAttributeItemRepository;

	@Resource
	private PricingService pricingService;

	@Resource
	private ProductAttributeService productAttributeService;


	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

	public ShoppingCartServiceImpl(ShoppingCartDao shoppingCartDao) {
		super(shoppingCartDao);
		this.shoppingCartDao = shoppingCartDao;

	}

	/**
	 * Retrieve a {@link ShoppingCartItem} cart for a given customer
	 */
	@Override
	@Transactional
	public ShoppingCartItem getShoppingCart(final CustomerItem customer) throws ServiceException {

		try {

			ShoppingCartItem shoppingCart = shoppingCartDao.findByCustomer(customer.getUuid());
			getPopulatedShoppingCart(shoppingCart);
			if (shoppingCart != null && (shoppingCart.getObsolete() == null || shoppingCart.getObsolete())) {
				delete(shoppingCart);
				return null;
			} else {
				return shoppingCart;
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Save or update a {@link ShoppingCartItem} for a given customer
	 */
	@Override
	public void saveOrUpdate(final ShoppingCartItem shoppingCart) throws ServiceException {

		super.save(shoppingCart);
	}

	/**
	 * Get a {@link ShoppingCartItem} for a given id and MerchantStoreItem. Will update
	 * the shopping cart prices and items based on the actual inventory. This
	 * method will remove the shopping cart if no items are attached.
	 */
	@Override
	public ShoppingCartItem getById(final UUID id, final MerchantStoreItem store) throws ServiceException {

		try {
			ShoppingCartItem shoppingCart = shoppingCartDao.findById(store.getUuid(), id);
			if (shoppingCart == null) {
				return null;
			}
			getPopulatedShoppingCart(shoppingCart);

			if (shoppingCart.getObsolete() != null ? shoppingCart.getObsolete() : false) {
				delete(shoppingCart);
				return null;
			} else {
				return shoppingCart;
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Get a {@link ShoppingCartItem} for a given id. Will update the shopping cart
	 * prices and items based on the actual inventory. This method will remove
	 * the shopping cart if no items are attached.
	 *
	 * @param id
	 */
	@Override
	public ShoppingCartItem getById(final UUID id) {

		try {
			ShoppingCartItem shoppingCart = shoppingCartDao.findOne(id);
			if (shoppingCart == null) {
				return null;
			}
			getPopulatedShoppingCart(shoppingCart);

			if (shoppingCart.getObsolete()) {
				delete(shoppingCart);
				return null;
			} else {
				return shoppingCart;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Get a {@link ShoppingCartItem} for a given code. Will update the shopping
	 * cart prices and items based on the actual inventory. This method will
	 * remove the shopping cart if no items are attached.
	 */
	@Override
	@Transactional
	public ShoppingCartItem getByCode(final String code, final MerchantStoreItem store) throws ServiceException {

		return shoppingCartDao.findByCode(store.getUuid(), code);

	}

	@Override
	public void deleteCart(final ShoppingCartItem shoppingCart) {
		ShoppingCartItem cart = this.getById(shoppingCart.getUuid());
		if (cart != null) {
			super.delete(cart);
		}
	}

	@Override
	public ShoppingCartItem getByCustomer(final CustomerItem customer) throws ServiceException {

		try {
			ShoppingCartItem shoppingCart = shoppingCartDao.findByCustomer(customer.getUuid());
			if (shoppingCart == null) {
				return null;
			}
			return getPopulatedShoppingCart(shoppingCart);

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private ShoppingCartItem getPopulatedShoppingCart(final ShoppingCartItem shoppingCart) throws Exception {

		try {

			boolean cartIsObsolete = false;
			if (shoppingCart != null) {

				Set<ShoppingCartEntryItem> items = shoppingCart.getLineItems();
				if (items == null || items.size() == 0) {
					shoppingCart.setObsolete(true);
					return shoppingCart;

				}

				// Set<ShoppingCartEntryItem> shoppingCartItems = new
				// HashSet<ShoppingCartEntryItem>();
				for (ShoppingCartEntryItem item : items) {
					LOGGER.debug("Populate item " + item.getUuid());
					getPopulatedItem(item);
					LOGGER.debug("Obsolete item ? " + item.getObsolete());
					if (item.getObsolete() != null ? item.getObsolete() : false) {
						cartIsObsolete = true;
					}
				}

				// shoppingCart.setLineItems(shoppingCartItems);
				boolean refreshCart = false;
				Set<ShoppingCartEntryItem> refreshedItems = new HashSet<ShoppingCartEntryItem>();
				for (ShoppingCartEntryItem item : items) {
/*					if (!item.getObsolete()) {
						refreshedItems.add(item);
					} else {
						refreshCart = true;
					}*/
					refreshedItems.add(item);
				}

				//if (refreshCart) {
				shoppingCart.setLineItems(refreshedItems);
				update(shoppingCart);
				//}

				if (cartIsObsolete) {
					shoppingCart.setObsolete(true);
				}
				return shoppingCart;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ServiceException(e);
		}

		return shoppingCart;

	}

	@Override
	public ShoppingCartEntryItem populateShoppingCartItem(final ProductItem product) throws ServiceException {
		Validate.notNull(product, "ProductItem should not be null");
		Validate.notNull(product.getMerchantStore(), "ProductItem.merchantStore should not be null");

		ShoppingCartEntryItem item = new ShoppingCartEntryItem();
		item.setProduct(product);

		// Set<ProductAttributeItem> productAttributes = product.getAttributes();
		// Set<ShoppingCartEntryAttributeItem> attributesList = new
		// HashSet<ShoppingCartEntryAttributeItem>();
		// if(!CollectionUtils.isEmpty(productAttributes)) {

		// for(ProductAttributeItem productAttribute : productAttributes) {
		// ShoppingCartEntryAttributeItem attributeItem = new
		// ShoppingCartEntryAttributeItem();
		// attributeItem.setShoppingCartItem(item);
		// attributeItem.setProductAttribute(productAttribute);
		// attributeItem.setProductAttributeId(productAttribute.getUuid());
		// attributesList.add(attributeItem);

		// }

		// item.setAttributes(attributesList);
		// }

		item.setProductVirtual(product.getProductVirtual());

		// set item price
		FinalPrice price = pricingService.calculateProductPrice(product);
		item.setItemPrice(price.getFinalPrice());
		return item;

	}

	private void getPopulatedItem(final ShoppingCartEntryItem item) throws Exception {

		ProductItem product = null;

		product = item.getProduct();

		if (product == null) {
			item.setObsolete(true);
			return;
		}

		item.setProduct(product);

		if (product.getProductVirtual() != null ? product.getProductVirtual() : false) {
			item.setProductVirtual(true);
		}

		Set<ShoppingCartEntryAttributeItem> cartAttributes = item.getAttributes();
		Set<ProductAttributeItem> productAttributes = product.getAttributes();
		List<ProductAttributeItem> attributesList = new ArrayList<ProductAttributeItem>();//attributes maintained
		List<ShoppingCartEntryAttributeItem> removeAttributesList = new ArrayList<ShoppingCartEntryAttributeItem>();//attributes to remove
		//DELETE ORPHEANS MANUALLY
		if ((productAttributes != null && productAttributes.size() > 0) || (cartAttributes != null && cartAttributes.size() > 0)) {
			for (ShoppingCartEntryAttributeItem attribute : cartAttributes) {
				UUID attributeId = attribute.getProductAttribute().getUuid();
				boolean existingAttribute = false;
				for (ProductAttributeItem productAttribute : productAttributes) {

					if (productAttribute.getUuid().equals(attributeId)) {
						attribute.setProductAttribute(productAttribute);
						attributesList.add(productAttribute);
						existingAttribute = true;
						break;
					}
				}

				if (!existingAttribute) {
					removeAttributesList.add(attribute);
				}

			}
		}

		//cleanup orphean item
		if (CollectionUtils.isNotEmpty(removeAttributesList)) {
			for (ShoppingCartEntryAttributeItem attr : removeAttributesList) {
				shoppingCartAttributeItemRepository.delete(attr);
			}
		}

		//cleanup detached attributes
		if (CollectionUtils.isEmpty(attributesList)) {
			item.getAttributes().clear();
		}


		// set item price
		FinalPrice price = pricingService.calculateProductPrice(product, attributesList);
		item.setItemPrice(price.getFinalPrice());

		BigDecimal subTotal = item.getItemPrice().multiply(new BigDecimal(item.getQuantity().intValue()));
		item.setSubTotal(subTotal);

	}

	@Override
	public List<ShippingProduct> createShippingProduct(final ShoppingCartItem cart) throws ServiceException {
		/**
		 * Determines if products are virtual
		 */
		Set<ShoppingCartEntryItem> items = cart.getLineItems();
		List<ShippingProduct> shippingProducts = null;
		for (ShoppingCartEntryItem item : items) {
			ProductItem product = item.getProduct();
			if (!product.getProductVirtual() && product.getProductShippable()) {
				if (shippingProducts == null) {
					shippingProducts = new ArrayList<>();
				}
				ShippingProduct shippingProduct = new ShippingProduct(product);
				shippingProduct.setQuantity(item.getQuantity());
				var finalPrice = pricingService.calculateProductPrice(item.getProduct(), item.getAttributes().stream().map(ShoppingCartEntryAttributeItem::getProductAttribute).collect(Collectors.toList()));
				shippingProduct.setFinalPrice(finalPrice);
				shippingProducts.add(shippingProduct);
			}
		}

		return shippingProducts;

	}

	@Override
	public boolean isFreeShoppingCart(final ShoppingCartItem cart) throws ServiceException {
		/**
		 * Determines if products are free
		 */
		Set<ShoppingCartEntryItem> items = cart.getLineItems();
		for (ShoppingCartEntryItem item : items) {
			ProductItem product = item.getProduct();
			FinalPrice finalPrice = pricingService.calculateProductPrice(product);
			if (finalPrice.getFinalPrice().longValue() > 0) {
				return false;
			}
		}

		return true;

	}

	@Override
	public boolean requiresShipping(final ShoppingCartItem cart) throws ServiceException {

		Validate.notNull(cart, "Shopping cart cannot be null");
		Validate.notNull(cart.getLineItems(), "ShoppingCartItem items cannot be null");
		boolean requiresShipping = false;
		for (ShoppingCartEntryItem item : cart.getLineItems()) {
			ProductItem product = item.getProduct();
			if (product.getProductShippable()) {
				requiresShipping = true;
				break;
			}
		}

		return requiresShipping;

	}

	@Override
	public void removeShoppingCart(final ShoppingCartItem cart) throws ServiceException {
		shoppingCartDao.delete(cart);
	}

	@Override
	public ShoppingCartItem mergeShoppingCarts(final ShoppingCartItem userShoppingModel, final ShoppingCartItem sessionCart,
											   final MerchantStoreItem store) throws Exception {
		if (sessionCart.getCustomerId() != null && sessionCart.getCustomerId() == userShoppingModel.getCustomerId()) {
			LOGGER.info("Session Shopping cart belongs to same logged in user");
			if (CollectionUtils.isNotEmpty(userShoppingModel.getLineItems())
					&& CollectionUtils.isNotEmpty(sessionCart.getLineItems())) {
				return userShoppingModel;
			}
		}

		LOGGER.info("Starting merging shopping carts");
		if (CollectionUtils.isNotEmpty(sessionCart.getLineItems())) {
			Set<ShoppingCartEntryItem> shoppingCartItemsSet = getShoppingCartItems(sessionCart, store, userShoppingModel);
			boolean duplicateFound = false;
			if (CollectionUtils.isNotEmpty(shoppingCartItemsSet)) {
				for (ShoppingCartEntryItem sessionShoppingCartItem : shoppingCartItemsSet) {
					if (CollectionUtils.isNotEmpty(userShoppingModel.getLineItems())) {
						for (ShoppingCartEntryItem cartItem : userShoppingModel.getLineItems()) {
							if (cartItem.getProduct().getUuid().equals(sessionShoppingCartItem.getProduct()
									.getUuid())) {
								if (CollectionUtils.isNotEmpty(cartItem.getAttributes())) {
									if (!duplicateFound) {
										LOGGER.info("Dupliate item found..updating exisitng product quantity");
										cartItem.setQuantity(
												cartItem.getQuantity() + sessionShoppingCartItem.getQuantity());
										duplicateFound = true;
										break;
									}
								}
							}
						}
					}
					if (!duplicateFound) {
						LOGGER.info("New item found..adding item to Shopping cart");
						userShoppingModel.getLineItems().add(sessionShoppingCartItem);
					}
				}

			}

		}
		LOGGER.info("Shopping Cart merged successfully.....");
		saveOrUpdate(userShoppingModel);
		removeShoppingCart(sessionCart);

		return userShoppingModel;
	}

	private Set<ShoppingCartEntryItem> getShoppingCartItems(final ShoppingCartItem sessionCart, final MerchantStoreItem store,
															final ShoppingCartItem cartModel) throws Exception {

		Set<ShoppingCartEntryItem> shoppingCartItemsSet = null;
		if (CollectionUtils.isNotEmpty(sessionCart.getLineItems())) {
			shoppingCartItemsSet = new HashSet<>();
			for (ShoppingCartEntryItem shoppingCartItem : sessionCart.getLineItems()) {
				ProductItem product = productService.getById(shoppingCartItem.getProduct().getUuid());
				if (product == null) {
					throw new Exception("Item with id " + shoppingCartItem.getProduct().getUuid() + " does not exist");
				}

				if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
					throw new Exception("Item with id " + shoppingCartItem.getProduct().getUuid()
							+ " does not belong to merchant " + store.getUuid());
				}

				ShoppingCartEntryItem item = populateShoppingCartItem(product);
				item.setQuantity(shoppingCartItem.getQuantity());
				item.setShoppingCart(cartModel);

				List<ShoppingCartEntryAttributeItem> cartAttributes = new ArrayList<>(
						shoppingCartItem.getAttributes());
				if (CollectionUtils.isNotEmpty(cartAttributes)) {
					for (ShoppingCartEntryAttributeItem shoppingCartAttributeItem : cartAttributes) {
						ProductAttributeItem productAttribute = productAttributeService
								.getById(shoppingCartAttributeItem.getUuid());
						if (productAttribute != null
								&& productAttribute.getProduct().getUuid().equals(product.getUuid())) {

							ShoppingCartEntryAttributeItem attributeItem = new ShoppingCartEntryAttributeItem();
							attributeItem.setShoppingCartItem(item);
							attributeItem.setProductAttribute(productAttribute);
							if (shoppingCartAttributeItem.getUuid() != null) {
								attributeItem.setUuid(shoppingCartAttributeItem.getUuid());
							}
							var attributes = item.getAttributes();

							if (CollectionUtils.isEmpty(attributes)) {
								attributes = Sets.newHashSet();
							}
							attributes.add(attributeItem);
							item.setAttributes(attributes);

						}
					}
				}

				shoppingCartItemsSet.add(item);
			}

		}
		return shoppingCartItemsSet;
	}

	@Override
	public boolean isFreeShoppingCart(List<ShoppingCartEntryItem> items) throws ServiceException {
		ShoppingCartItem cart = new ShoppingCartItem();
		Set<ShoppingCartEntryItem> cartItems = new HashSet<>(items);
		cart.setLineItems(cartItems);
		return this.isFreeShoppingCart(cart);
	}

	@Override
	public void deleteShoppingCartItem(UUID id) {
		shoppingCartItemDao.delete(shoppingCartItemDao.find(id));
	}

}
