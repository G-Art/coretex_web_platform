import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs"
import {CartData} from "../data/cart.data";
import {environment} from "../../../environments/environment";
import {DeliveryServiceData} from "../data/delivery-service.data";
import {map, share} from "rxjs/operators";

@Injectable()
export class DeliveryServiceService {

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {
    }

    getForCart(cart: CartData): Observable<DeliveryServiceData[]> {

        return this.http.get<DeliveryServiceData[]>(`${this.apiUrl + '/delivery/service/cart/' + cart.uuid}`, {
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
