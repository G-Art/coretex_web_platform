import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {map, share} from "rxjs/operators";
import {CategoryData} from "../data/category.data";
import {App} from '../../app.constants';

@Injectable()
export class CategoryService {

  data:CategoryData[];
  observable;

  constructor(private http: HttpClient) {
  }

  getCategoriesMenu(): Observable<CategoryData[]> {
    if (this.data) {
      return of(this.data);
    } else if (this.observable) {
      return this.observable;
    } else {
      this.observable = this.http.get<CategoryData[]>(App.API.menu, {
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
