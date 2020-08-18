import {Component, ElementRef, EventEmitter, HostBinding, Input, OnInit, Output, ViewChild} from '@angular/core';
import {ProductData} from "../../../core/data/product.data";
import {ProductService} from "../../../core/service/product.service";
import {ProductVariantData} from "../../../core/data/product-variant.data";
import {CartService} from "../../../core/service/cart.service";
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";

declare var $: any;

@Component({
    animations : [fadeInAnimation],
    host: { '[@fadeInAnimation]': '' },
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
        for (let style of this._product.variants) {
            for (let size of style.variants) {
                if(size.code === this._product.defaultVariantCode){
                    this.displayStyleVariant = style;
                    this.displaySizeVariant = size;
                    break;
                }
            }
        }
        if(!this.displayStyleVariant){
            this.displayStyleVariant = this._product.variants.find(style => this);
            if(this.displayStyleVariant){
                this.displaySizeVariant = this.displayStyleVariant.variants.find(o => true);
            }
        }
    }

    displayStyleVariant: ProductVariantData;
    displaySizeVariant: ProductVariantData;

    @ViewChild('imageWrapper', {static: false})
    imageWrapper: ElementRef;
    imageLoader: boolean = true;

    constructor(private productService: ProductService, private cartService: CartService) {
    }

    ngOnInit() {

    }

    setDisplayStyleVariant(variant: ProductVariantData) {
        if(this.displayStyleVariant !== variant){
            this.imageLoader = true;
            this.displayStyleVariant = variant;
            this.displaySizeVariant = this.displayStyleVariant.variants.find(o => true);
        }
    }

    setColumns(value: string) {
        this.columnsClass = value;
    }

    setItemView(value: string) {
        this.itemView = value
    }

    displayQuickView() {
        this.productService.showQuickViewFor(this.displayStyleVariant, this.imageWrapper, this.product)
    }

    addToCart() {
        if (!this.atcClicked) {
            this.atcClicked = true;
            if (this.displayStyleVariant.variants.length > 1) {
                this.cartService.addToCart(this.displayStyleVariant.variants[0], 1, () => {
                    this.atcClicked = false;
                });
            } else {
                this.cartService.addToCart(this.displaySizeVariant, 1, () => {
                    this.atcClicked = false;
                });
            }

        }
    }
}
