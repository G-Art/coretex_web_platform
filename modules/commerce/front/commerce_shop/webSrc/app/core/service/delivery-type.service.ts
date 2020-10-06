import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, share} from "rxjs/operators";
import {DeliveryServiceData} from "../data/delivery-service.data";
import {DeliveryTypeData} from "../data/delivery-type.data";
import {PaymentType} from '../data/payment-type.data';
import {App} from '../../app.constants';

@Injectable()
export class DeliveryTypeService {

    constructor(private http: HttpClient) {
    }

    getDeliveryTypeForService(deliveryServiceData: DeliveryServiceData): Observable<DeliveryTypeData[]> {
        return this.http.get<DeliveryTypeData[]>(App.API.deliveryServiceTypes(deliveryServiceData.uuid), {
            observe: 'response'
        }).pipe(
            map(response => {
                if (response.status === 400) {
                    return null;
                } else if (response.status === 200) {
                    return response.body;
                }
            }),
            share()
        );

    }

    getPaymentTypesForDeliveryType(deliveryType: DeliveryTypeData): Observable<PaymentType[]> {
        return this.http.get<DeliveryTypeData[]>(App.API.deliveryTypePayments(deliveryType.code), {
            observe: 'response'
        }).pipe(
            map(response => {
                if (response.status === 400) {
                    return null;
                } else if (response.status === 200) {
                    return response.body;
                }
            }),
            share()
        );
    }

}
