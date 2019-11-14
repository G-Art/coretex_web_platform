package com.coretex.newpost.api.enchiridion;

import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.NewPostApiRequestService;
import com.coretex.newpost.api.NewPostDataApiService;
import com.coretex.newpost.api.data.ResponseApiData;
import com.coretex.newpost.api.enchiridion.data.properties.CargoDescriptionProperties;
import com.coretex.newpost.api.enchiridion.data.properties.PackProperties;
import com.coretex.newpost.api.enchiridion.data.properties.TimeIntervalsProperties;
import com.coretex.newpost.api.enchiridion.data.values.BackwardDeliveryCargoTypeValue;
import com.coretex.newpost.api.enchiridion.data.values.CargoDescriptionValue;
import com.coretex.newpost.api.enchiridion.data.values.CargoTypeValue;
import com.coretex.newpost.api.enchiridion.data.values.MessageCodeTextValue;
import com.coretex.newpost.api.enchiridion.data.values.OwnershipFormValue;
import com.coretex.newpost.api.enchiridion.data.values.PackValue;
import com.coretex.newpost.api.enchiridion.data.values.PalletValue;
import com.coretex.newpost.api.enchiridion.data.values.PaymentFormValue;
import com.coretex.newpost.api.enchiridion.data.values.ServiceTypeValue;
import com.coretex.newpost.api.enchiridion.data.values.TimeIntervalValue;
import com.coretex.newpost.api.enchiridion.data.values.TiresWheelValue;
import com.coretex.newpost.api.enchiridion.data.values.TypesOfCounterpartyValue;
import com.coretex.newpost.api.enchiridion.data.values.TypesOfPayerValue;

public abstract class NewPostEnchiridionsApiService extends NewPostApiRequestService implements NewPostDataApiService {

	public abstract ResponseApiData<TimeIntervalValue> getTimeIntervals(NewPostDeliveryServiceItem deliveryServiceItem, TimeIntervalsProperties timeIntervalsProperties);

	public abstract ResponseApiData<CargoTypeValue> getCargoTypes(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<BackwardDeliveryCargoTypeValue> getBackwardDeliveryCargoTypes(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<PalletValue> getPalletsList(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<TypesOfPayerValue> getTypesOfPayers(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<TypesOfPayerValue> getTypesOfPayersForRedelivery(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<PackValue> getPackList(NewPostDeliveryServiceItem deliveryServiceItem, PackProperties packProperties);

	public abstract ResponseApiData<TiresWheelValue> getTiresWheelsList(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<CargoDescriptionValue> getCargoDescriptionList(NewPostDeliveryServiceItem deliveryServiceItem, CargoDescriptionProperties cargoDescriptionProperties);

	public abstract ResponseApiData<MessageCodeTextValue> getMessageCodeText(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<ServiceTypeValue> getServiceTypes(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<TypesOfCounterpartyValue> getTypesOfCounterparties(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<PaymentFormValue> getPaymentForms(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<OwnershipFormValue> getOwnershipFormsList(NewPostDeliveryServiceItem deliveryServiceItem);
}
