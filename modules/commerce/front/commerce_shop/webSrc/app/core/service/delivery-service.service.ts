import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs"
import {CartData} from "../data/cart.data";
import {DeliveryServiceData} from "../data/delivery-service.data";
import {map, share} from "rxjs/operators";
import {App} from '../../app.constants';

@Injectable()
export class DeliveryServiceService {

    constructor(private http: HttpClient) {
    }

    getForCart(cart: CartData): Observable<DeliveryServiceData[]> {
        return this.http.get<DeliveryServiceData[]>(App.API.deliveryServiceCart(cart.uuid), {
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
