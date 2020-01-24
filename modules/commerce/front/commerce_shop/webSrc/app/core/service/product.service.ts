import {ElementRef, EventEmitter, Injectable} from '@angular/core';
import {ProductData} from "../data/product.data";
import {QuickViewData} from "../data/quick-view.data";

@Injectable()
export class ProductService {
    productQuickView: EventEmitter<QuickViewData>;

    constructor() {
        this.productQuickView = new EventEmitter<QuickViewData>();
    }


    showQuickViewFor(product: ProductData, imageWrapper: ElementRef) {
        let qw = new QuickViewData();
        qw.product = product;
        qw.imageWrapper = imageWrapper;
        this.productQuickView.emit(qw)
    }

}
