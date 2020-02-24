import {Component, OnInit} from '@angular/core';
import {MiniCartData} from "../../../core/data/mini-cart.data";
import {CartService} from "../../../core/service/cart.service";
import {CartEntryData} from "../../../core/data/cart-entry.data";

@Component({
    selector: 'app-mini-cart',
    templateUrl: './mini-cart.component.html',
    styleUrls: ['./mini-cart.component.scss']
})
export class MiniCartComponent implements OnInit {

    miniCart: MiniCartData = new MiniCartData();

    constructor(private cartService: CartService) {
    }

    ngOnInit() {
        this.cartService.getCurrentCart().subscribe(cart => {

            this.miniCart = cart;

            let productCount: number = 0;

            for (const entry of this.miniCart.entries) {
                productCount += entry.quantity;
            }
            this.miniCart.productCount = productCount;
            return this.miniCart
        });
        this.cartService.updateMiniCart.subscribe(cart => {
            if (cart) {
                this.miniCart = cart;

                let productCount: number = 0;

                for (const entry of this.miniCart.entries) {
                    productCount += entry.quantity;
                }

                this.miniCart.productCount = productCount
            } else {
                this.miniCart = new MiniCartData();
            }
        })
    }

    removeEntry(entry:CartEntryData) {
        this.cartService.addToCart(entry.product, 0);
    }

}
