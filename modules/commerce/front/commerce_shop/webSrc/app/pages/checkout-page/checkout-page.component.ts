import {Component, OnInit} from '@angular/core';
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";
import {CartData} from "../../core/data/cart.data";
import {CartService} from "../../core/service/cart.service";
import {Router} from "@angular/router";

@Component({
    animations: [fadeInAnimation],
    host: {'[@fadeInAnimation]': ''},
    selector: 'app-checkout-page',
    templateUrl: './checkout-page.component.html',
    styleUrls: ['./checkout-page.component.scss']
})
export class CheckoutPageComponent implements OnInit {
    private cartUpdate = false;
    cart: CartData;

    constructor(private cartService: CartService,
                private router: Router) {
    }

    ngOnInit(): void {
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

    placeOrder(){
        this.cartService.placeOrder()
    }

}
