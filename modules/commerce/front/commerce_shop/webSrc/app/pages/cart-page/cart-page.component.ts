import {Component, OnInit} from '@angular/core';
import {CartService} from "../../core/service/cart.service";
import {CartData} from "../../core/data/cart.data";
import {Route, Router} from "@angular/router";
import {CartEntryData} from "../../core/data/cart-entry.data";
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";

@Component({
    animations : [fadeInAnimation],
    host: { '[@fadeInAnimation]': '' },
    selector: 'app-cart-page',
    templateUrl: './cart-page.component.html',
    styleUrls: ['./cart-page.component.scss']
})
export class CartPageComponent implements OnInit {

    private cartUpdate = false;
    cart: CartData;

    constructor(private cartService: CartService,
                private router: Router) {
    }

    ngOnInit() {
        this.cartService
            .currentCart
            .subscribe(cart => {
                if (cart && cart.entries && cart.entries.length > 0) {
                    this.cart = cart;

                    let productCount: number = 0;

                    for (const entry of this.cart.entries) {
                        productCount += entry.quantity;
                    }

                    this.cart.productCount = productCount
                } else {
                    this.router.navigate([`/`])
                }
            });
    }

    decQuantity(entry: CartEntryData) {
        if (!this.cartUpdate) {
            if (entry.quantity > 0) {
                entry.quantity--;
            }
            this.cartService.updateEntryQuantity(entry, () => {
                this.cartUpdate = false;
            })
        }
    }

    incQuantity(entry: CartEntryData) {
        if (!this.cartUpdate) {
            entry.quantity++;
            this.cartService.updateEntryQuantity(entry, () => {
                this.cartUpdate = false;
            })
        }
    }

    qtyChange(entry: CartEntryData) {
        if (!this.cartUpdate) {
            if (entry.quantity < 0) {
                entry.quantity = 0;
            }
            this.cartService.updateEntryQuantity(entry, () => {
                this.cartUpdate = false;
            })
        }
    }

    removeEntry(entry: CartEntryData) {
        if (!this.cartUpdate) {
            entry.quantity = 0;
            this.cartService.updateEntryQuantity(entry, () => {
                this.cartUpdate = false;
            })
        }
    }
}
