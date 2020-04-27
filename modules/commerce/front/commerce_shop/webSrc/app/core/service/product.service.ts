import {ElementRef, EventEmitter, Injectable} from '@angular/core';
import {ProductData} from '../data/product.data';
import {QuickViewData} from '../data/quick-view.data';
import {ProductDetailData} from '../data/product-detail.data';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {map, share} from 'rxjs/operators';
import {environment} from '../../../environments/environment';
import {CartService} from './cart.service';

@Injectable()
export class ProductService {

    productQuickView: EventEmitter<QuickViewData>;

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient, private cartService: CartService) {
        this.productQuickView = new EventEmitter<QuickViewData>();
    }

    showQuickViewFor(product: ProductData, imageWrapper: ElementRef, baseProduct: ProductData) {
        let qw = new QuickViewData();
        qw.product = product;
        qw.baseProduct = baseProduct;
        qw.imageWrapper = imageWrapper;
        this.productQuickView.emit(qw)
    }

    getProductDetail(productCode: string): Observable<ProductDetailData> {
        return this.http.get<ProductDetailData>(`${this.apiUrl + '/product/' + productCode}`, {
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

}
