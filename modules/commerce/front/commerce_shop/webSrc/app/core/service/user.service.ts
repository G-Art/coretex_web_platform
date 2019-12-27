import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
export class UserService {
    constructor(private http: HttpClient) {}

    authenticate(name: string, password: string):Observable<any> {
        return this.http
            .post( `${environment.baseApiUrl}/login`, {name, password});
    }

    getUsers(): Observable<any> {
        return this.http.get(`${environment.baseApiUrl}/users`);
    }

    getCurrentUser(): Observable<any> {
        return this.http.get(`${environment.baseApiUrl}/user/current`);
    }

}