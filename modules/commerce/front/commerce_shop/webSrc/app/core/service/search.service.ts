import {Injectable} from '@angular/core';
import {SearchResult} from "../data/search.result.data";
import {ProductData} from "../data/product.data";
import {Observable, of} from "rxjs";
import {map, share} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable()
export class SearchService {

    apiUrl = environment.baseApiUrl;

    searchResult: SearchResult;

    constructor(private http: HttpClient) {
        let prods = [];

        for (let i = 0; i < 12; i++) {
            let product = new ProductData();
            product.uuid = i.toString();
            product.code = 'code_' + i;

            product.images = [
                {path: '/app/assets/img/products/product-9-1-600x800.jpg'},
                {path: '/app/assets/img/products/product-9-2-600x800.jpg'}
            ];

            product.title = 'Test Product_' + i;
            product.price = '$' + ((i + 1) * 100);
            if (Math.random() >= 0.5) {
                product.priceDiscount = '$' + ((i + 1) * 10);
            }
            product.hotBadge = Math.random() >= 0.5;
            product.discountBadgeInfo = '-' + Math.round(Math.random() * 10) + '%';

            product.description = 'Lorem ipsum dolor sit amet, consectetur\n' +
                '            adipisicing elit. Dolorem quod optio quaerat in molestiae amet repudiandae\n' +
                '            repellendus eveniet libero mollitia. Lorem ipsum dolor sit amet, consectetur\n' +
                '            adipisicing elit.';

            prods.push(product)
        }

        this.searchResult = {
            breadcrumb: [{
                link: '/',
                text: 'Home'
            },{
                active: true,
                text: 'Technical'
            }
            ],
            page: 0,
            totalPages: 1,
            count: 12,
            totalCount:12,
            products: prods
        } as SearchResult

    }

    searchCategory(val: string): Observable<SearchResult> {

            // return  this.http.get<any>(`${this.apiUrl}/languages/store/`, {
            //     observe: 'response'
            // }).pipe(
            //     map(response => {
            //         if (response.status === 400) {
            //             return 'Request failed.';
            //         } else if (response.status === 200) {
            //             return response.body;
            //         }
            //     }),
            //     share()
            // );
        return of(this.searchResult);
    }

    searchQuery(val: string): Observable<SearchResult> {
        return of(this.searchResult);
    }

}
