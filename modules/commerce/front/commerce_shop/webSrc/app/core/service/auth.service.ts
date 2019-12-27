import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

import {Router} from "@angular/router";
import {Observable, of} from "rxjs";
import {UserForm} from "../data/user-form.data";
import {UserService} from "./user.service";

@Injectable()
export class AuthService {
    redirectUrl: string;

    constructor(private userService: UserService,
                private cookieService: CookieService,
                private router: Router) {


    }


    isAuthenticated(): boolean {
        return !!this.cookieService.get('auth');
    }

    login(user: UserForm) {
        let auth = this.userService
            .authenticate(user.login, user.password);

        auth.subscribe(() => {
            this.cookieService.set('auth', 'true');
        });

        return auth
    }

    logout(): void {
        this.cookieService.delete('auth');
        this.router.navigate([`/`]);
    }

    getRedirectUrl(): Observable<any> {
        let url: string = this.redirectUrl || '/';
        return of(url);
    }

    forbidden() {
        this.router.navigate(['403']);
    }

}