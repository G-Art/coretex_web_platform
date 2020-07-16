import {Injectable} from '@angular/core';
import {SearchResult} from '../data/search.result.data';
import {ActivatedRoute} from '@angular/router';
import {Observable, Subject} from 'rxjs';
import {map, share} from 'rxjs/operators';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable()
export class SearchService {

    type: string = 'category';

    apiUrl = environment.baseApiUrl;

    searchResult: Subject<SearchResult> = new Subject();

    constructor(private http: HttpClient) {

    }

    searchCategory(code: string, page?: number, facets?: Map<string, string[]>): void {

        let params = new HttpParams();
        if (facets) {
            facets.forEach((values, key) => {
                values.forEach(value => {
                    params = params.append(`f(${key})`, value);
                })
            })
        }

        this.http.get<any>(`${this.apiUrl}/categories/${code}/page${page ? '/' + page : ''}`, {
            params: params,
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
        ).subscribe(next => this.searchResult.next(next));
        // return of(this.searchResult);
    }

}
