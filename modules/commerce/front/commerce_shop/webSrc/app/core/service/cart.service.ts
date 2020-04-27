import {EventEmitter, Injectable, OnInit} from '@angular/core';
import {ProductVariantData} from '../data/product-variant.data';
import {HttpClient} from '@angular/common/http';
import {map, share} from 'rxjs/operators';
import {environment} from '../../../environments/environment';
import {CartData} from '../data/cart.data';
import {BehaviorSubject, Observable} from 'rxjs';
import {CartEntryData} from '../data/cart-entry.data';
import {DeliveryTypeData} from '../data/delivery-type.data';

@Injectable()
export class CartService implements OnInit {

    private _currentCart: BehaviorSubject<CartData>;
    currentCart: Observable<CartData>;

    submitOrder: EventEmitter<any>;

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {
        this._currentCart = new BehaviorSubject<CartData>(undefined);
        this.currentCart = this._currentCart.asObservable();
        this.submitOrder = new EventEmitter<any>();
        this.updateCurrentCart();
    }

    ngOnInit(): void {
    }

    updateCurrentCart(): void {
        this.http.get<CartData>(`${this.apiUrl + '/cart/current'}`, {
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
        ).subscribe(cart => {
            this._currentCart.next(cart)
        });
    }

    updateEntryQuantity(entry: CartEntryData, next?: () => void) {
        this.http.post<CartData>(`${this.apiUrl + '/cart/update'}`,
            {entry: entry.uuid, quantity: entry.quantity},
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
            this._currentCart.next(data)
            if (next) {
                next()
            }
        });
    }

    addToCart(product: ProductVariantData, qty: number, next?: () => void) {
        this.http.post<CartData>(`${this.apiUrl + '/cart/add'}`,
            {product: product.uuid, quantity: qty},
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
            this._currentCart.next(data)
            if (next) {
                next()
            }
        });

    }

    addDeliveryType(deliveryType?: DeliveryTypeData, next?: () => void) {
        this.http.post<CartData>(`${this.apiUrl + '/cart/delivery/type'}`,
            {uuid: deliveryType ? deliveryType.uuid : null},
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
            this._currentCart.next(data)
            if (next) {
                next()
            }
        });
    }

    placeOrder() {
        this.submitOrder.emit();
    }

    addDeliveryInfo(value: any) {
        this.http.post<CartData>(`${this.apiUrl + '/cart/delivery/info'}`,
            value,
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
            this._currentCart.next(data)
        });
    }
}
