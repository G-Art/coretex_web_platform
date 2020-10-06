import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {map, share} from 'rxjs/operators';
import {StoreData} from '../data/store.data';
import {App} from '../../app.constants';

@Injectable()
export class StoreService {
    data: StoreData;
    observable;

    constructor(private http: HttpClient) {
    }

    getCurrentStore(): Observable<StoreData> {
        if (this.data) {
            return of(this.data);
        } else if (this.observable) {
            return this.observable;
        } else {
            this.observable = this.http.get(App.API.currentStore, {
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
