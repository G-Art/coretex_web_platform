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

    }

    searchCategory(code: string, page?:number): Observable<SearchResult> {

            return this.http.get<any>(`${this.apiUrl}/categories/${code}/page${page ? '/'+page : ''}`, {
                observe: 'response'
            }).pipe(
                map(response => {
                    if (response.status === 400) {
                        return 'Request failed.';
                    } else if (response.status === 200) {
                        return response.body;
                    }
                }),
                share()
            );
        // return of(this.searchResult);
    }

    searchQuery(val: string): Observable<SearchResult> {
        return of(this.searchResult);
    }

}
