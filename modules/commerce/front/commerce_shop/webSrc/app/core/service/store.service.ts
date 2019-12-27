import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {map, share} from "rxjs/operators";
import {StoreData} from "../data/store.data";

@Injectable()
export class StoreService {
  data:StoreData;
  observable;

  apiUrl = environment.baseApiUrl;

  constructor(private http: HttpClient) {
  }

  getCurrentStore(): Observable<StoreData> {
    if (this.data) {
      return of(this.data);
    } else if (this.observable) {
      return this.observable;
    } else {
      this.observable = this.http.get(`${this.apiUrl + '/stores/current'}`, {
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
