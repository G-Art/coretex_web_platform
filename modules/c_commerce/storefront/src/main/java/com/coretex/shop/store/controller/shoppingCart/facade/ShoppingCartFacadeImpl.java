
package com.coretex.shop.store.controller.shoppingCart.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.shoppingcart.CartModificationException;
import com.coretex.shop.model.shoppingcart.PersistableShoppingCartItem;
import com.coretex.shop.model.shoppingcart.ReadableShoppingCart;
import com.coretex.shop.model.shoppingcart.ShoppingCartAttribute;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;
import com.coretex.shop.populator.shoppingCart.ReadableShoppingCartPopulator;
import com.coretex.shop.populator.shoppingCart.ShoppingCartDataPopulator;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.ImageFilePath;

/**
 * @author Umesh Awasthi
 * @version 1.0
 * @since 1.0
 */
@Service(value = "shoppingCartFacade")
public class ShoppingCartFacadeImpl
		implements ShoppingCartFacade {


	private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartFacadeImpl.class);

	@Resource
	private ShoppingCartService shoppingCartService;

	@Resource
	private ShoppingCartCalculationService shoppingCartCalculationService;

	@Resource
	private ProductPriceUtils productPriceUtils;

	@Resource
	private ProductService productService;

	@Resource
	private PricingService pricingService;

	@Resource
	private ProductAttributeService productAttributeService;

	@Resource
	private LanguageService languageService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	private ShoppingCartDataPopulator shoppingCartDataPopulator;

	@PostConstruct
	private void init() {
		shoppingCartDataPopulator = new ShoppingCartDataPopulator();
		shoppingCartDataPopulator.setLanguageService(languageService);
		shoppingCartDataPopulator.setShoppingCartCalculationService(shoppingCartCalculationService);
		shoppingCartDataPopulator.setPricingService(pricingService);
		shoppingCartDataPopulator.setimageUtils(imageUtils);
	}

	public void deleteShoppingCart(final UUID id, final MerchantStoreItem store) throws Exception {
		ShoppingCartItem cart = shoppingCartService.getById(id, store);
		if (cart != null) {
			shoppingCartService.deleteCart(cart);
		}
	}

	@Override
	public void deleteShoppingCart(final String code, final MerchantStoreItem store) throws Exception {
		ShoppingCartItem cart = shoppingCartService.getByCode(code, store);
		if (cart != null) {
			shoppingCartService.deleteCart(cart);
		}
	}

	@Override
	public ShoppingCartData addItemsToShoppingCart(final ShoppingCartData shoppingCartData,
												   final com.coretex.shop.model.shoppingcart.ShoppingCartItem item, final MerchantStoreItem store, final LocaleItem language, final CustomerItem customer)
			throws Exception {

		ShoppingCartItem cartModel = null;

		/**
		 * Sometimes a user logs in and a shopping cart is present in db (shoppingCartData
		 * but ui has no cookie with shopping cart code so the cart code will have
		 * to be added to the item in order to process add to cart normally
		 */
		if (shoppingCartData != null && StringUtils.isBlank(item.getCode())) {
			item.setCode(shoppingCartData.getCode());
		}


		if (!StringUtils.isBlank(item.getCode())) {
			// get it from the db
			cartModel = getShoppingCartModel(item.getCode(), store);
			if (cartModel == null) {
				cartModel = createCartModel(shoppingCartData.getCode(), store, customer);
			}

		}

		if (cartModel == null) {

			final String shoppingCartCode =
					StringUtils.isNotBlank(shoppingCartData.getCode()) ? shoppingCartData.getCode() : null;
			cartModel = createCartModel(shoppingCartCode, store, customer);

		}

		ShoppingCartEntryItem shoppingCartItem =
				createCartItem(cartModel, item, store);

		boolean duplicateFound = false;
		if (CollectionUtils.isEmpty(item.getShoppingCartAttributes())) {//increment quantity
			//get duplicate item from the cart
			Set<ShoppingCartEntryItem> cartModelItems = cartModel.getLineItems();
			for (ShoppingCartEntryItem cartItem : cartModelItems) {
				if (cartItem.getProduct().getUuid().equals(shoppingCartItem.getProduct().getUuid())) {
					if (!duplicateFound) {
						if (!(shoppingCartItem.getProductVirtual() != null ? shoppingCartItem.getProductVirtual() : false)) {
							cartItem.setQuantity(cartItem.getQuantity() + shoppingCartItem.getQuantity());
						}
						duplicateFound = true;
						break;
					}
				}
			}
		}

		if (!duplicateFound) {
			cartModel.getLineItems().add(shoppingCartItem);
		}

		/** Update cart in database with line items **/
		shoppingCartService.saveOrUpdate(cartModel);
		shoppingCartService.refresh(cartModel);

		shoppingCartCalculationService.calculate(cartModel, store, language);

		return shoppingCartDataPopulator.populate(cartModel, store, language);
	}

	private ShoppingCartEntryItem createCartItem(final ShoppingCartItem cartModel,
												 final com.coretex.shop.model.shoppingcart.ShoppingCartItem shoppingCartItem,
												 final MerchantStoreItem store)
			throws Exception {

		ProductItem product = productService.getByUUID(shoppingCartItem.getProductId());

		if (product == null) {
			throw new Exception("Item with id " + shoppingCartItem.getProductId() + " does not exist");
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			throw new Exception("Item with id " + shoppingCartItem.getProductId() + " does not belong to merchant "
					+ store.getUuid());
		}

		/**
		 * Check if product quantity is 0
		 * Check if product is available
		 * Check if date available <= now
		 */

		Set<ProductAvailabilityItem> availabilities = product.getAvailabilities();
		if (availabilities == null) {

			throw new Exception("Item with id " + product.getUuid() + " is not properly configured");

		}

		for (ProductAvailabilityItem availability : availabilities) {
			if (availability.getProductQuantity() == null || availability.getProductQuantity().intValue() == 0) {
				throw new Exception("Item with id " + product.getUuid() + " is not available");
			}
		}

		if (!product.getAvailable()) {
			throw new Exception("Item with id " + product.getUuid() + " is not available");
		}

		if (!DateUtil.dateBeforeEqualsDate(product.getDateAvailable(), new Date())) {
			throw new Exception("Item with id " + product.getUuid() + " is not available");
		}


		ShoppingCartEntryItem item =
				shoppingCartService.populateShoppingCartItem(product);

		item.setQuantity(shoppingCartItem.getQuantity());
		item.setShoppingCart(cartModel);

		return item;

	}


	//used for api
	private ShoppingCartEntryItem createCartItem(ShoppingCartItem cartModel,
												 PersistableShoppingCartItem shoppingCartItem, MerchantStoreItem store) throws Exception {

		ProductItem product = productService.getByUUID(shoppingCartItem.getProduct());

		if (product == null) {
			throw new Exception("Item with id " + shoppingCartItem.getProduct() + " does not exist");
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			throw new Exception("Item with id " + shoppingCartItem.getProduct() + " does not belong to merchant "
					+ store.getUuid());
		}

		/**
		 * Check if product quantity is 0
		 * Check if product is available
		 * Check if date available <= now
		 */

		Set<ProductAvailabilityItem> availabilities = product.getAvailabilities();
		if (availabilities == null) {

			throw new Exception("Item with id " + product.getUuid() + " is not properly configured");

		}

		for (ProductAvailabilityItem availability : availabilities) {
			if (availability.getProductQuantity() == null || availability.getProductQuantity().intValue() == 0) {
				throw new Exception("Item with id " + product.getUuid() + " is not available");
			}
		}

		if (!product.getAvailable()) {
			throw new Exception("Item with id " + product.getUuid() + " is not available");
		}

		if (!DateUtil.dateBeforeEqualsDate(product.getDateAvailable(), new Date())) {
			throw new Exception("Item with id " + product.getUuid() + " is not available");
		}


		ShoppingCartEntryItem item = shoppingCartService
				.populateShoppingCartItem(product);

		item.setQuantity(shoppingCartItem.getQuantity());
		item.setShoppingCart(cartModel);

		return item;

	}


	@Override
	public ShoppingCartItem createCartModel(final String shoppingCartCode, final MerchantStoreItem store, final CustomerItem customer) {
		final UUID CustomerId = customer != null ? customer.getUuid() : null;
		ShoppingCartItem cartModel = new ShoppingCartItem();
		if (StringUtils.isNotBlank(shoppingCartCode)) {
			cartModel.setShoppingCartCode(shoppingCartCode);
		} else {
			cartModel.setShoppingCartCode(uniqueShoppingCartCode());
		}

		cartModel.setMerchantStore(store);
		if (CustomerId != null) {
			cartModel.setCustomerId(CustomerId);
		}
		shoppingCartService.create(cartModel);
		return cartModel;
	}


	private ShoppingCartEntryItem getEntryToUpdate(final UUID entryId,
												   final ShoppingCartItem cartModel) {
		if (CollectionUtils.isNotEmpty(cartModel.getLineItems())) {
			for (ShoppingCartEntryItem shoppingCartItem : cartModel.getLineItems()) {
				if (shoppingCartItem.getUuid().equals(entryId)) {
					LOG.info("Found line item  for given entry id: " + entryId);
					return shoppingCartItem;

				}
			}
		}
		LOG.info("Unable to find any entry for given Id: " + entryId);
		return null;
	}

	private Object getKeyValue(final String key) {
		ServletRequestAttributes reqAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return reqAttr.getRequest().getAttribute(key);
	}

	@Override
	public ShoppingCartData getShoppingCartData(final CustomerItem customer, final MerchantStoreItem store,
												final String shoppingCartId, LocaleItem language)
			throws Exception {

		ShoppingCartItem cart = null;
		if (customer != null) {
			LOG.info("Reteriving customer shopping cart...");

			cart = shoppingCartService.getShoppingCart(customer);

		} else {
			if (StringUtils.isNotBlank(shoppingCartId) && cart == null) {
				cart = shoppingCartService.getByCode(shoppingCartId, store);
			}

		}

		if (cart == null) {
			return null;
		}

		LOG.info("Cart model found.");

		//LocaleItem language = (LocaleItem) getKeyValue( Constants.LANGUAGE );
		MerchantStoreItem merchantStore = (MerchantStoreItem) getKeyValue(Constants.MERCHANT_STORE);

		ShoppingCartData shoppingCartData = shoppingCartDataPopulator.populate(cart, merchantStore, language);
        
/*        List<ShoppingCartEntryItem> unavailables = new ArrayList<ShoppingCartEntryItem>();
        List<ShoppingCartEntryItem> availables = new ArrayList<ShoppingCartEntryItem>();
        //Take out items no more available
        List<ShoppingCartEntryItem> items = shoppingCartData.getShoppingCartItems();
        for(ShoppingCartEntryItem item : items) {
        	String code = item.getProductCode();
        	ProductItem p =productService.getByCode(code, language);
        	if(!p.isAvailable()) {
        		unavailables.add(item);
        	} else {
        		availables.add(item);
        	}
        	
        }
        shoppingCartData.setShoppingCartItems(availables);
        shoppingCartData.setUnavailables(unavailables);*/

		return shoppingCartData;

	}

	//@Override
	public ShoppingCartData getShoppingCartData(final ShoppingCartItem shoppingCartModel, LocaleItem language)
			throws Exception {
		MerchantStoreItem merchantStore = (MerchantStoreItem) getKeyValue(Constants.MERCHANT_STORE);
		return shoppingCartDataPopulator.populate(shoppingCartModel, merchantStore, language);
	}

	@Override
	public ShoppingCartData removeCartItem(final UUID itemID, final String cartId, final MerchantStoreItem store, final LocaleItem language)
			throws Exception {
		if (StringUtils.isNotBlank(cartId)) {

			ShoppingCartItem cartModel = getCartModel(cartId, store);
			if (cartModel != null) {
				if (CollectionUtils.isNotEmpty(cartModel.getLineItems())) {
					Set<ShoppingCartEntryItem> shoppingCartItemSet =
							new HashSet<ShoppingCartEntryItem>();
					for (ShoppingCartEntryItem shoppingCartItem : cartModel.getLineItems()) {
						//if ( shoppingCartItem.getUuid().longValue() != itemID.longValue() )
						if (shoppingCartItem.getUuid().equals(itemID)) {
							//shoppingCartItemSet.add( shoppingCartItem );
							shoppingCartService.deleteShoppingCartItem(itemID);
						}
					}


					cartModel = getCartModel(cartId, store);

					if (cartModel == null) {
						return null;
					}
					return shoppingCartDataPopulator.populate(cartModel, store, language);
				}
			}
		}
		return null;
	}

	@Override
	public ShoppingCartData updateCartItem(final UUID itemID, final String cartId, final long newQuantity, final MerchantStoreItem store, final LocaleItem language)
			throws Exception {
		if (newQuantity < 1) {
			throw new CartModificationException("Quantity must not be less than one");
		}
		if (StringUtils.isNotBlank(cartId)) {
			ShoppingCartItem cartModel = getCartModel(cartId, store);
			if (cartModel != null) {
				ShoppingCartEntryItem entryToUpdate =
						getEntryToUpdate(itemID, cartModel);

				if (entryToUpdate == null) {
					throw new CartModificationException("Unknown entry number.");
				}

				entryToUpdate.getProduct();

				LOG.info("Updating cart entry quantity to" + newQuantity);
				entryToUpdate.setQuantity((int) newQuantity);
				List<ProductAttributeItem> productAttributes = new ArrayList<ProductAttributeItem>();
				productAttributes.addAll(entryToUpdate.getProduct().getAttributes());
				final FinalPrice finalPrice =
						productPriceUtils.getFinalProductPrice(entryToUpdate.getProduct(), productAttributes);
				entryToUpdate.setItemPrice(finalPrice.getFinalPrice());
				shoppingCartService.saveOrUpdate(cartModel);

				LOG.info("Cart entry updated with desired quantity");
				return shoppingCartDataPopulator.populate(cartModel, store, language);

			}
		}
		return null;
	}

	@Override
	public ShoppingCartData updateCartItems(final List<com.coretex.shop.model.shoppingcart.ShoppingCartItem> shoppingCartItems, final MerchantStoreItem store, final LocaleItem language)
			throws Exception {

		Validate.notEmpty(shoppingCartItems, "shoppingCartItems null or empty");
		ShoppingCartItem cartModel = null;
		Set<ShoppingCartEntryItem> cartItems = new HashSet<ShoppingCartEntryItem>();
		for (com.coretex.shop.model.shoppingcart.ShoppingCartItem item : shoppingCartItems) {

			if (item.getQuantity() < 1) {
				throw new CartModificationException("Quantity must not be less than one");
			}

			if (cartModel == null) {
				cartModel = getCartModel(item.getCode(), store);
			}

			ShoppingCartEntryItem entryToUpdate =
					getEntryToUpdate(item.getUuid(), cartModel);

			if (entryToUpdate == null) {
				throw new CartModificationException("Unknown entry number.");
			}

			entryToUpdate.getProduct();

			LOG.info("Updating cart entry quantity to" + item.getQuantity());
			entryToUpdate.setQuantity(item.getQuantity());

			List<ProductAttributeItem> productAttributes = new ArrayList<ProductAttributeItem>();
			productAttributes.addAll(entryToUpdate.getProduct().getAttributes());

			final FinalPrice finalPrice =
					productPriceUtils.getFinalProductPrice(entryToUpdate.getProduct(), productAttributes);
			entryToUpdate.setItemPrice(finalPrice.getFinalPrice());


			cartItems.add(entryToUpdate);


		}

		cartModel.setLineItems(cartItems);
		shoppingCartService.saveOrUpdate(cartModel);
		LOG.info("Cart entry updated with desired quantity");
		return shoppingCartDataPopulator.populate(cartModel, store, language);

	}


	private ShoppingCartItem getCartModel(final String cartId, final MerchantStoreItem store) {
		if (StringUtils.isNotBlank(cartId)) {
			return shoppingCartService.getByCode(cartId, store);
		}
		return null;
	}

	@Override
	public ShoppingCartData getShoppingCartData(String code, MerchantStoreItem store, LocaleItem language) {
		try {
			ShoppingCartItem cartModel = shoppingCartService.getByCode(code, store);
			if (cartModel != null) {
				ShoppingCartData cart = getShoppingCartData(cartModel, language);
				return cart;
			}
		} catch (Exception e) {
			LOG.error("Cannot retrieve cart code " + code, e);
		}


		return null;
	}

	@Override
	public ShoppingCartItem getShoppingCartModel(String shoppingCartCode,
												 MerchantStoreItem store) {
		return shoppingCartService.getByCode(shoppingCartCode, store);
	}

	@Override
	public ShoppingCartItem getShoppingCartModel(CustomerItem customer,
												 MerchantStoreItem store) throws Exception {
		return shoppingCartService.getByCustomer(customer);
	}

	@Override
	public void saveOrUpdateShoppingCart(ShoppingCartItem cart) throws Exception {
		shoppingCartService.saveOrUpdate(cart);

	}

	@Override
	public ReadableShoppingCart getCart(CustomerItem customer, MerchantStoreItem store, LocaleItem language) throws Exception {

		Validate.notNull(customer, "CustomerItem cannot be null");
		Validate.notNull(customer.getUuid(), "CustomerItem.id cannot be null or empty");

		//Check if customer has an existing shopping cart
		ShoppingCartItem cartModel = shoppingCartService.getByCustomer(customer);

		if (cartModel == null) {
			return null;
		}

		shoppingCartCalculationService.calculate(cartModel, store, language);

		ReadableShoppingCartPopulator readableShoppingCart = new ReadableShoppingCartPopulator();

		readableShoppingCart.setImageUtils(imageUtils);
		readableShoppingCart.setPricingService(pricingService);
		readableShoppingCart.setProductAttributeService(productAttributeService);
		readableShoppingCart.setShoppingCartCalculationService(shoppingCartCalculationService);

		ReadableShoppingCart readableCart = new ReadableShoppingCart();

		readableShoppingCart.populate(cartModel, readableCart, store, language);


		return readableCart;
	}

	@Override
	public ReadableShoppingCart addToCart(PersistableShoppingCartItem item, MerchantStoreItem store,
										  LocaleItem language) throws Exception {

		Validate.notNull(item, "PersistableShoppingCartItem cannot be null");

		//if cart does not exist create a new one

		ShoppingCartItem cartModel = new ShoppingCartItem();
		cartModel.setMerchantStore(store);
		cartModel.setShoppingCartCode(uniqueShoppingCartCode());


		return readableShoppingCart(cartModel, item, store, language);
	}

	private ReadableShoppingCart readableShoppingCart(ShoppingCartItem cartModel, PersistableShoppingCartItem item, MerchantStoreItem store,
													  LocaleItem language) throws Exception {

		saveShoppingCart(cartModel);

		//refresh cart
		cartModel = shoppingCartService.getById(cartModel.getUuid(), store);

		shoppingCartCalculationService.calculate(cartModel, store, language);

		ReadableShoppingCartPopulator readableShoppingCart = new ReadableShoppingCartPopulator();

		readableShoppingCart.setImageUtils(imageUtils);
		readableShoppingCart.setPricingService(pricingService);
		readableShoppingCart.setProductAttributeService(productAttributeService);
		readableShoppingCart.setShoppingCartCalculationService(shoppingCartCalculationService);

		ReadableShoppingCart readableCart = new ReadableShoppingCart();

		readableShoppingCart.populate(cartModel, readableCart, store, language);


		return readableCart;

	}


	private ReadableShoppingCart modifyCart(ShoppingCartItem cartModel, PersistableShoppingCartItem item, MerchantStoreItem store,
											LocaleItem language) throws Exception {


		ShoppingCartEntryItem itemModel = createCartItem(cartModel, item, store);


		//check if existing product
		Set<ShoppingCartEntryItem> items = cartModel.getLineItems();
		//com.coretex.core.model.shoppingcart.ShoppingCartEntryItem affectedItem = null;
		if (!CollectionUtils.isEmpty(items)) {
			Set<ShoppingCartEntryItem> newItems = new HashSet<ShoppingCartEntryItem>();
			Set<ShoppingCartEntryItem> removeItems = new HashSet<ShoppingCartEntryItem>();
			for (ShoppingCartEntryItem anItem : items) {//take care of existing product
				if (itemModel.getProduct().getUuid().equals(anItem.getProduct().getUuid())) {
					if (item.getQuantity() == 0) {//left aside item to be removed
						//don't add it to new list of item
						removeItems.add(anItem);
					} else {
						//new quantity
						anItem.setQuantity(item.getQuantity());
						newItems.add(anItem);
					}
				} else {
					newItems.add(itemModel);
				}
			}

			if (!removeItems.isEmpty()) {
				for (ShoppingCartEntryItem emptyItem : removeItems) {
					shoppingCartService.deleteShoppingCartItem(emptyItem.getUuid());
				}

			}

			if (newItems.isEmpty()) {
				newItems = null;
			}
			cartModel.setLineItems(newItems);
		} else {
			//new item
			if (item.getQuantity() > 0) {
				cartModel.getLineItems().add(itemModel);
			}
		}

		//if cart items are null just return cart with no items

		saveShoppingCart(cartModel);

		//refresh cart
		cartModel = shoppingCartService.getById(cartModel.getUuid(), store);

		if (cartModel == null) {
			return null;
		}

		shoppingCartCalculationService.calculate(cartModel, store, language);

		ReadableShoppingCartPopulator readableShoppingCart = new ReadableShoppingCartPopulator();

		readableShoppingCart.setImageUtils(imageUtils);
		readableShoppingCart.setPricingService(pricingService);
		readableShoppingCart.setProductAttributeService(productAttributeService);
		readableShoppingCart.setShoppingCartCalculationService(shoppingCartCalculationService);

		ReadableShoppingCart readableCart = new ReadableShoppingCart();

		readableShoppingCart.populate(cartModel, readableCart, store, language);


		return readableCart;

	}

	@Override
	public ReadableShoppingCart addToCart(CustomerItem customer, PersistableShoppingCartItem item, MerchantStoreItem store,
										  LocaleItem language) throws Exception {

		Validate.notNull(customer, "CustomerItem cannot be null");
		Validate.notNull(customer.getUuid(), "CustomerItem.id cannot be null or empty");

		//Check if customer has an existing shopping cart
		ShoppingCartItem cartModel = shoppingCartService.getByCustomer(customer);

		//if cart does not exist create a new one
		if (cartModel == null) {
			cartModel = new ShoppingCartItem();
			cartModel.setCustomerId(customer.getUuid());
			cartModel.setMerchantStore(store);
			cartModel.setShoppingCartCode(uniqueShoppingCartCode());
		}

		return readableShoppingCart(cartModel, item, store, language);
	}

	@Override
	public ReadableShoppingCart addToCart(String cartCode, PersistableShoppingCartItem item, MerchantStoreItem store,
										  LocaleItem language) throws Exception {

		Validate.notNull(cartCode, "PString cart code cannot be null");
		Validate.notNull(item, "PersistableShoppingCartItem cannot be null");

		ShoppingCartItem cartModel = this.getCartModel(cartCode, store);


		return modifyCart(cartModel, item, store, language);


	}

	private void saveShoppingCart(ShoppingCartItem shoppingCart) throws Exception {
		shoppingCartService.save(shoppingCart);
	}

	private String uniqueShoppingCartCode() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	@Override
	public ReadableShoppingCart getById(UUID shoppingCartId, MerchantStoreItem store, LocaleItem language) throws Exception {

		ShoppingCartItem cart = shoppingCartService.getByUUID(shoppingCartId);

		ReadableShoppingCart readableCart = null;

		if (cart != null) {

			ReadableShoppingCartPopulator readableShoppingCart = new ReadableShoppingCartPopulator();

			readableShoppingCart.setImageUtils(imageUtils);
			readableShoppingCart.setPricingService(pricingService);
			readableShoppingCart.setProductAttributeService(productAttributeService);
			readableShoppingCart.setShoppingCartCalculationService(shoppingCartCalculationService);

			readableShoppingCart.populate(cart, readableCart, store, language);


		}

		return readableCart;
	}

	@Override
	public ShoppingCartItem getShoppingCartModel(UUID id, MerchantStoreItem store) throws Exception {
		return shoppingCartService.getByUUID(id);
	}

	@Override
	public ReadableShoppingCart getByCode(String code, MerchantStoreItem store, LocaleItem language) throws Exception {

		ShoppingCartItem cart = shoppingCartService.getByCode(code, store);

		ReadableShoppingCart readableCart = null;

		if (cart != null) {

			ReadableShoppingCartPopulator readableShoppingCart = new ReadableShoppingCartPopulator();

			readableShoppingCart.setImageUtils(imageUtils);
			readableShoppingCart.setPricingService(pricingService);
			readableShoppingCart.setProductAttributeService(productAttributeService);
			readableShoppingCart.setShoppingCartCalculationService(shoppingCartCalculationService);

			readableCart = readableShoppingCart.populate(cart, null, store, language);


		}

		return readableCart;

	}


}
