import {EventEmitter, Injectable} from '@angular/core';
import {ProductVariantData} from "../data/product-variant.data";
import {HttpClient} from "@angular/common/http";
import {map, share} from "rxjs/operators";
import {environment} from "../../../environments/environment";
import {MiniCartData} from "../data/mini-cart.data";
import {QuickViewData} from "../data/quick-view.data";
import {Observable, of} from "rxjs";
import {StoreData} from "../data/store.data";

@Injectable()
export class CartService {

    updateMiniCart: EventEmitter<MiniCartData>;

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {
        this.updateMiniCart = new EventEmitter<MiniCartData>();
    }

    getCurrentCart(): Observable<MiniCartData> {
        return this.http.get<MiniCartData>(`${this.apiUrl + '/cart/current'}`, {
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

    addToCart(product: ProductVariantData, qty: number) {
        this.http.post<MiniCartData>(`${this.apiUrl + '/cart/add'}`,
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
            this.updateMiniCart.emit(data);
        });

    }
}
