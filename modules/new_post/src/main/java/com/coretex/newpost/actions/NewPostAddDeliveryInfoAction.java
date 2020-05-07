package com.coretex.newpost.actions;

import com.coretex.commerce.delivery.api.actions.AddDeliveryInfoAction;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.items.cx_core.AddressItem;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
public class NewPostAddDeliveryInfoAction implements AddDeliveryInfoAction {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ItemService itemService;

	@Override
	public void execute(CartItem cartItem, Map<String, Object> info) {

		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Add NewPost delivery info for cart [%s], info:[%s]", cartItem.getUuid(), info));
		}

		AddressItem addressItem;
		if (Objects.nonNull(cartItem.getAddress())) {
			addressItem = cartItem.getAddress();
		} else {
			addressItem = itemService.create(AddressItem.class);
		}

		addressItem.setFirstName(FieldExtractor.FIRST_NAME.extract(info));
		addressItem.setLastName(FieldExtractor.LAST_NAME.extract(info));
		addressItem.setCity(FieldExtractor.CITY.extract(info));
		addressItem.setNewPostCityRef(FieldExtractor.CITY_REF.extract(info));
		addressItem.setAddressLine1(FieldExtractor.ADDRESS_LINE1.extract(info));
		addressItem.setAddressLine2(FieldExtractor.ADDRESS_LINE2.extract(info));
		addressItem.setPhone(FieldExtractor.PHONE.extract(info));
		addressItem.setPostalCode(FieldExtractor.ZIP_CODE.extract(info));
		addressItem.setNewPostWarehouse(FieldExtractor.BRANCH.extract(info));
		addressItem.setNewPostWarehouseRef(FieldExtractor.BRANCH_REF.extract(info));
		addressItem.setEmail(FieldExtractor.EMAIL.extract(info));

		cartItem.setAddress(addressItem);

		var createAccount = FieldExtractor.CREATE_ACCOUNT.extract(info);
		if (Objects.nonNull(createAccount) && BooleanUtils.isTrue((Boolean) createAccount) && Objects.isNull(cartItem.getCustomer())) {
			CustomerItem customerItem = itemService.create(CustomerItem.class);
			customerItem.setAnonymous(false);
			customerItem.setEmail(FieldExtractor.EMAIL.extract(info));
			customerItem.setFirstName(FieldExtractor.FIRST_NAME.extract(info));
			customerItem.setLastName(FieldExtractor.LAST_NAME.extract(info));
			customerItem.setActive(false);
			customerItem.getAddresses().add(addressItem);
			customerItem.setDelivery(addressItem);
			cartItem.setCustomer(customerItem);
		}

		itemService.save(cartItem);
	}

	@Override
	public String deliveryType() {
		return NewPostDeliveryTypeItem.ITEM_TYPE;
	}

	public enum FieldExtractor {
		DELIVERY_TYPE("deliveryType"),
		FIRST_NAME("firstName"),
		LAST_NAME("lastName"),
		EMAIL("email"),
		PHONE("phone"),
		ADDRESS_LINE1("addressLine1"),
		ADDRESS_LINE2("addressLine2"),
		COUNTRY("country"),
		CITY("city"),
		CITY_REF("cityRef"),
		ZIP_CODE("zipCode"),
		BRANCH("branch"),
		BRANCH_REF("branchRef"),
		CREATE_ACCOUNT("createAccount");

		FieldExtractor(String infoField) {
			this.infoField = infoField;
		}

		FieldExtractor(String infoField, BiFunction<String, Map<String, Object>, Object> customExtractor) {
			this(infoField);
			this.customExtractor = customExtractor;
		}

		private final String infoField;
		private BiFunction<String, Map<String, Object>, Object> customExtractor;

		<R> R extract(Map<String, Object> info) {
			if (Objects.nonNull(customExtractor)) {
				return (R) customExtractor.apply(infoField, info);
			}
			return (R) info.getOrDefault(infoField, null);
		}

		public String getInfoField() {
			return infoField;
		}

	}
}
