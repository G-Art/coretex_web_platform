import {EventEmitter, Injectable} from '@angular/core';
import {ProductVariantData} from "../data/product-variant.data";
import {HttpClient} from "@angular/common/http";
import {map, share} from "rxjs/operators";
import {environment} from "../../../environments/environment";
import {CartData} from "../data/cart.data";
import {Observable, of} from "rxjs";
import {CartEntryData} from "../data/cart-entry.data";

@Injectable()
export class CartService {

    updateCart: EventEmitter<CartData>;

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {
        this.updateCart = new EventEmitter<CartData>();
    }

    getCurrentCart(): Observable<CartData> {
        return this.http.get<CartData>(`${this.apiUrl + '/cart/current'}`, {
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


    updateEntryQuantity(entry : CartEntryData, next?: () => void){
        this.http.post<CartData>(`${this.apiUrl + '/cart/update'}`,
            {"entry": entry.uuid, "quantity": entry.quantity},
            {
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
        ).subscribe(data => {
            this.updateCart.emit(data);
            if(next){
                next()
            }
        });
    }

    addToCart(product: ProductVariantData, qty: number, next?: () => void) {
        this.http.post<CartData>(`${this.apiUrl + '/cart/add'}`,
            {"product": product.uuid, "quantity": qty},
            {
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
        ).subscribe(data => {
            this.updateCart.emit(data);
            if(next){
                next()
            }
        });

    }
}
