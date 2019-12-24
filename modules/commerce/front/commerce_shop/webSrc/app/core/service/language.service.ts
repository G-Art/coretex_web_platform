import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, share} from "rxjs/operators";
import {LanguageData} from "../data/language.data";

@Injectable()
export class LanguageService {

    data;
    observable;

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {

    }

    getStorageLanguages(uuid: string): Observable<LanguageData[]> {

        if (this.data) {
            return new Observable(this.data);
        } else if (this.observable) {
            return this.observable;
        } else {
            this.observable = this.http.get(`${this.apiUrl}/languages/store/${uuid}`, {
                observe: 'response'
            }).pipe(
                map(response => {
                    this.observable = null;
                    if (response.status === 400) {
                        return 'Request failed.';
                    } else if (response.status === 200) {
                        this.data = response.body;
                        return this.data;
                    }
                }),
                share()
            );
            return this.observable;
        }

    }
}
