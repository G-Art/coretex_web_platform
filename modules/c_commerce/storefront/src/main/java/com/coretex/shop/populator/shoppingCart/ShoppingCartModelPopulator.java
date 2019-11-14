
package com.coretex.shop.populator.shoppingCart;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryAttributeItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.shop.model.shoppingcart.ShoppingCartAttribute;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;
import com.google.api.client.util.Sets;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Umesh A
 */

@Service(value = "shoppingCartModelPopulator")
public class ShoppingCartModelPopulator
		extends AbstractDataPopulator<ShoppingCartData, ShoppingCartItem> {

	private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartModelPopulator.class);

	private ShoppingCartService shoppingCartService;

	private CustomerItem customer;

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}


	public void setShoppingCartService(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}


	private ProductService productService;


	public ProductService getProductService() {
		return productService;
	}


	public void setProductService(ProductService productService) {
		this.productService = productService;
	}


	private ProductAttributeService productAttributeService;


	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}


	public void setProductAttributeService(
			ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}


	@Override
	public ShoppingCartItem populate(ShoppingCartData shoppingCart, ShoppingCartItem cartMdel, final MerchantStoreItem store, LanguageItem language) {


		// if id >0 get the original from the database, override products
		try {
			if (shoppingCart.getUuid() != null) {
				cartMdel = shoppingCartService.getByCode(shoppingCart.getCode(), store);
				if (cartMdel == null) {
					cartMdel = new ShoppingCartItem();
					cartMdel.setShoppingCartCode(shoppingCart.getCode());
					cartMdel.setMerchantStore(store);
					if (customer != null) {
						cartMdel.setCustomerId(customer.getUuid());
					}
					shoppingCartService.create(cartMdel);
				}
			} else {
				cartMdel.setShoppingCartCode(shoppingCart.getCode());
				cartMdel.setMerchantStore(store);
				if (customer != null) {
					cartMdel.setCustomerId(customer.getUuid());
				}
				shoppingCartService.create(cartMdel);
			}

			List<com.coretex.shop.model.shoppingcart.ShoppingCartItem> items = shoppingCart.getShoppingCartItems();
			Set<ShoppingCartEntryItem> newItems =
					new HashSet<ShoppingCartEntryItem>();
			if (items != null && items.size() > 0) {
				for (com.coretex.shop.model.shoppingcart.ShoppingCartItem item : items) {

					Set<ShoppingCartEntryItem> cartItems = cartMdel.getLineItems();
					if (cartItems != null && cartItems.size() > 0) {

						for (ShoppingCartEntryItem dbItem : cartItems) {
							if (dbItem.getUuid().equals(item.getUuid())) {
								dbItem.setQuantity(item.getQuantity());
								// compare attributes
								Set<ShoppingCartEntryAttributeItem> attributes =
										dbItem.getAttributes();
								Set<ShoppingCartEntryAttributeItem> newAttributes =
										new HashSet<>();
								List<ShoppingCartAttribute> cartAttributes = item.getShoppingCartAttributes();
								if (!CollectionUtils.isEmpty(cartAttributes)) {
									for (ShoppingCartAttribute attribute : cartAttributes) {
										for (ShoppingCartEntryAttributeItem dbAttribute : attributes) {
											if (dbAttribute.getUuid().equals(attribute.getUuid())) {
												newAttributes.add(dbAttribute);
											}
										}
									}

									dbItem.setAttributes(newAttributes);
								}
								newItems.add(dbItem);
							}
						}
					} else {// create new item
						ShoppingCartEntryItem cartItem =
								createCartItem(cartMdel, item, store);
						Set<ShoppingCartEntryItem> lineItems =
								cartMdel.getLineItems();
						if (lineItems == null) {
							lineItems = new HashSet<ShoppingCartEntryItem>();
							cartMdel.setLineItems(lineItems);
						}
						lineItems.add(cartItem);
						shoppingCartService.update(cartMdel);
					}
				}// end for
			}// end if
		} catch (ServiceException se) {
			LOG.error("Error while converting cart data to cart model.." + se);
			throw new ConversionException("Unable to create cart model", se);
		} catch (Exception ex) {
			LOG.error("Error while converting cart data to cart model.." + ex);
			throw new ConversionException("Unable to create cart model", ex);
		}

		return cartMdel;
	}


	private ShoppingCartEntryItem createCartItem(ShoppingCartItem cart,
												 com.coretex.shop.model.shoppingcart.ShoppingCartItem shoppingCartItem,
												 MerchantStoreItem store)
			throws Exception {

		ProductItem product = productService.getById(shoppingCartItem.getProductId());

		if (product == null) {
			throw new Exception("Item with id " + shoppingCartItem.getProductId() + " does not exist");
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			throw new Exception("Item with id " + shoppingCartItem.getProductId() + " does not belong to merchant "
					+ store.getUuid());
		}

		ShoppingCartEntryItem item = new ShoppingCartEntryItem();
		item.setShoppingCart(cart);
		item.setProduct(product);
		item.setQuantity(shoppingCartItem.getQuantity());
		item.setItemPrice(shoppingCartItem.getProductPrice());
		item.setShoppingCart(cart);

		// attributes
		List<ShoppingCartAttribute> cartAttributes = shoppingCartItem.getShoppingCartAttributes();
		if (!CollectionUtils.isEmpty(cartAttributes)) {
			for (ShoppingCartAttribute attribute : cartAttributes) {
				ProductAttributeItem productAttribute = productAttributeService.getById(attribute.getAttributeId());
				if (productAttribute != null
						&& productAttribute.getProduct().getUuid().equals(product.getUuid())) {
					ShoppingCartEntryAttributeItem attributeItem = new ShoppingCartEntryAttributeItem();
					attributeItem.setShoppingCartItem(item);
					attributeItem.setProductAttribute(productAttribute);

					attributeItem.setUuid(attribute.getUuid());

					var attributes = item.getAttributes();

					if (CollectionUtils.isEmpty(attributes)) {
						attributes = Sets.newHashSet();
					}

					attributes.add(attributeItem);

					item.setAttributes(attributes);
					//newAttributes.add( attributeItem );
				}
			}

			//item.setAttributes( newAttributes );
		}

		return item;

	}


	@Override
	protected ShoppingCartItem createTarget() {

		return new ShoppingCartItem();
	}


	public CustomerItem getCustomer() {
		return customer;
	}


	public void setCustomer(CustomerItem customer) {
		this.customer = customer;
	}


}
