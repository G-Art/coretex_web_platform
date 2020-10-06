import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {map, share} from 'rxjs/operators';
import {LanguageData} from '../data/language.data';
import {App} from '../../app.constants';

@Injectable()
export class LanguageService {

    dataMap = new Map();
    observable;

    constructor(private http: HttpClient) {

    }

    getStorageLanguages(uuid: string): Observable<LanguageData[]> {
        if (this.dataMap.has(uuid)) {
            return of(this.dataMap.get(uuid));
        } else if (this.observable) {
            return this.observable;
        } else {
            this.observable = this.http.get<LanguageData[]>(App.API.storeLanguages(uuid), {
                observe: 'response'
            }).pipe(
                map(response => {
                    this.observable = null;
                    if (response.status === 400) {
                        return 'Request failed.';
                    } else if (response.status === 200) {
                        const data = response.body;
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
