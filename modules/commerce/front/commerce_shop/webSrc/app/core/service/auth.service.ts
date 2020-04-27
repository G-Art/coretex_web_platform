import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

import {Router} from '@angular/router';
import {Observable, of} from 'rxjs';
import {LoginForm} from '../data/login-form.data';
import {UserService} from './user.service';

@Injectable()
export class AuthService {
    redirectUrl: string;

    constructor(private userService: UserService,
                private cookieService: CookieService,
                private router: Router) {
    }


    isAuthenticated(): boolean {
        return !!this.cookieService.get('accessToken');
    }

    login(user: LoginForm): Observable<any> {
        return this.userService
            .authenticate(user.login, user.password);
    }

    logout(): void {
        this.userService.logout().subscribe(ignore => {
            this.cookieService.delete('accessToken');
            this.userService.updateCurrentUser();
            this.router.navigate([`/`]);
        })
    }

    getRedirectUrl(): Observable<any> {
        let url: string = this.redirectUrl || '/';
        return of([url]);
    }

    forbidden() {
        this.router.navigate(['403']);
    }

}