import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {map, share} from "rxjs/operators";
import {LanguageData} from "../data/language.data";

@Injectable()
export class LanguageService {

    dataMap = new Map();
    observable;

    apiUrl = environment.baseApiUrl;

    constructor(private http: HttpClient) {

    }

    getStorageLanguages(uuid: string): Observable<LanguageData[]> {
        if (this.dataMap.has(uuid)) {
            return of(this.dataMap.get(uuid));
        } else if (this.observable) {
            return this.observable;
        } else {
            this.observable = this.http.get<LanguageData[]>(`${this.apiUrl}/languages/store/${uuid}`, {
                observe: 'response'
            }).pipe(
                map(response => {
                    this.observable = null;
                    if (response.status === 400) {
                        return 'Request failed.';
                    } else if (response.status === 200) {
                        let data = response.body;
                        this.dataMap.set(uuid, data);
                        return data;
                    }
                }),
                share()
            );
            return this.observable;
        }

    }
}
