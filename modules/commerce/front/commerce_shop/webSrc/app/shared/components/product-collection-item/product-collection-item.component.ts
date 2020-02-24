import {Component, ElementRef, EventEmitter, HostBinding, Input, OnInit, Output, ViewChild} from '@angular/core';
import {ProductData} from "../../../core/data/product.data";
import {ProductService} from "../../../core/service/product.service";
import {ProductVariantData} from "../../../core/data/product-variant.data";
import {CartService} from "../../../core/service/cart.service";

declare var $: any;

@Component({
    selector: 'app-product-collection-item',
    templateUrl: './product-collection-item.component.html',
    styleUrls: ['./product-collection-item.component.scss']
})
export class ProductCollectionItemComponent implements OnInit {


    @HostBinding('class')
    @Input()
    columnsClass: string = 'col-lg-4';

    @Input()
    itemView: string = 'grid';

    private _product: ProductData;

    private atcClicked: boolean = false;

    get product(): ProductData {
        return this._product;
    }

    @Input()
    set product(value: ProductData) {
        this._product = value;
        this.displayStyleVariant = this._product.variants.find(o => true);
        this.displaySizeVariant = this.displayStyleVariant.variants.find(o => true);
    }

    displayStyleVariant: ProductVariantData;
    displaySizeVariant: ProductVariantData;

    @ViewChild('imageWrapper', {static: false})
    imageWrapper: ElementRef;

    constructor(private productService: ProductService, private cartService: CartService) {
    }

    ngOnInit() {

    }

    setDisplayStyleVariant(variant: ProductVariantData) {
        this.displayStyleVariant = variant;
        this.displaySizeVariant = this.displayStyleVariant.variants.find(o => true);
    }

    setColumns(value: string) {
        this.columnsClass = value;
    }

    setItemView(value: string) {
        this.itemView = value
    }

    displayQuickView() {
        this.productService.showQuickViewFor(this.displayStyleVariant, this.imageWrapper)
    }

    addToCart() {
        if (!this.atcClicked) {
            this.atcClicked = true;
            if (this.displayStyleVariant.variants.length > 1) {
                this.cartService.addToCart(this.displayStyleVariant.variants[0], 1);
            } else {
                this.cartService.addToCart(this.displaySizeVariant, 1);
            }
            this.atcClicked = false;
        }
    }
}
