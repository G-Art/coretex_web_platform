import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, share} from "rxjs/operators";
import {environment} from "../../../environments/environment";
import {DeliveryServiceData} from "../data/delivery-service.data";
import {DeliveryTypeData} from "../data/delivery-type.data";
import {PaymentType} from '../data/payment-type.data';

@Injectable()
export class DeliveryTypeService {

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {
    }

    getDeliveryTypeForService(deliveryServiceData: DeliveryServiceData): Observable<DeliveryTypeData[]> {

        return this.http.get<DeliveryTypeData[]>(`${this.apiUrl + `/delivery/service/${deliveryServiceData.uuid}/types` }`, {
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
        return this.http.get<DeliveryTypeData[]>(`${this.apiUrl + `/delivery/type/${deliveryType.code}/payments` }`, {
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
