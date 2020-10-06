import {Component, OnInit} from '@angular/core';
import {CartData} from '../../../core/data/cart.data';
import {CartService} from '../../../core/service/cart.service';
import {CartEntryData} from '../../../core/data/cart-entry.data';

@Component({
    selector: 'app-mini-cart',
    templateUrl: './mini-cart.component.html',
    styleUrls: ['./mini-cart.component.scss']
})
export class MiniCartComponent implements OnInit {

    miniCart: CartData = new CartData();

    constructor(private cartService: CartService) {
    }

    ngOnInit() {
        this.cartService
            ._currentCart
            .subscribe(cart => {
                if (cart) {
                    this.miniCart = cart;

                    let productCount: number = 0;

                    for (const entry of this.miniCart.entries) {
                        productCount += entry.quantity;
                    }

                    this.miniCart.productCount = productCount
                } else {
                    this.miniCart = new CartData();
                    this.miniCart.productCount = 0;
                }
            });
    }

    removeEntry(entry: CartEntryData) {
        this.cartService.addToCart(entry.product, 0);
    }

}
