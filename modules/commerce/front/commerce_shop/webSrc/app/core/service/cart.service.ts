import {EventEmitter, Injectable, OnInit} from '@angular/core';
import {ProductVariantData} from '../data/product-variant.data';
import {HttpClient} from '@angular/common/http';
import {map, share} from 'rxjs/operators';
import {environment} from '../../../environments/environment';
import {CartData} from '../data/cart.data';
import {BehaviorSubject, Observable} from 'rxjs';
import {CartEntryData} from '../data/cart-entry.data';
import {DeliveryTypeData} from '../data/delivery-type.data';
import {PaymentType} from '../data/payment-type.data';

@Injectable()
export class CartService implements OnInit {

    private _currentCart: BehaviorSubject<CartData>;
    currentCart: Observable<CartData>;

    beforeOrderPlace: EventEmitter<any>;

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {
        this._currentCart = new BehaviorSubject<CartData>(undefined);
        this.currentCart = this._currentCart.asObservable();
        this.beforeOrderPlace = new EventEmitter<any>();
        this.updateCurrentCart()
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

    placeOrder(next?: () => void) {
        this.currentCart.subscribe(cart => {
            if (cart && cart.address && cart.paymentMode && cart.deliveryType) {
                this.finishOrderPlacement(cart)
                if (next) {
                    next();
                }
            }
        })
        this.beforeOrderPlace.emit();
    }

    private finishOrderPlacement(cart: CartData) {
        this.http.post<any>(`${this.apiUrl + '/cart/placeOrder'}`,
            null,
            {
                observe: 'response'
            }).pipe(
            map(response => {
                if (response.status === 200) {

                    return response.body;
                } else {
                    return null;
                }
            }),
            share()
        ).subscribe(data => {
            console.log('order placed')
            this.updateCurrentCart()
            //    ignore
        })
    }

    addDeliveryInfo(value: any, errorCode?: (code: number) => void) {
        this.http.post<CartData>(`${this.apiUrl + '/cart/delivery/info'}`,
            value,
            {
                observe: 'response'
            }).pipe(
            map(response => {
                if (response.status === 200) {

                    return response.body;
                } else {
                    errorCode(response.status)
                    return null;
                }
            }),
            share()
        ).subscribe(data => {
            this._currentCart.next(data)
        });
    }

    addPaymentType(payment: PaymentType, next?: () => void) {
        this.http.post<CartData>(`${this.apiUrl + '/cart/payment/type'}`,
            {uuid: payment ? payment.uuid : null},
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
}
