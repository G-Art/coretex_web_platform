import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {RegisterForm} from '../data/register-form.data';
import {UserData} from '../data/user.data';
import {share} from 'rxjs/operators';
import {CartService} from './cart.service';
import {App} from '../../app.constants';

@Injectable()
export class UserService implements OnInit {
    private _currentUser = new BehaviorSubject<UserData>(undefined);
    currentUser: Observable<UserData>;

    constructor(private http: HttpClient, private cartService: CartService) {
        this.currentUser = this._currentUser.asObservable().pipe(share());
    }

    ngOnInit(): void {
    }

    authenticate(name: string, password: string): Observable<any> {
        return this.http
            .post(App.API.login, {name, password})
            .pipe(share());
    }

    logout(): Observable<any> {
        return this.http
            .get(App.API.logout)
            .pipe(share());
    }

    register(registerForm: RegisterForm): Observable<any> {
        return this.http
            .post(App.API.signUp, registerForm)
            .pipe(share());
    }

    updateCurrentUser(): void {
        this.http.get<UserData>(App.API.currentUser)
            .toPromise()
            .then(user => {
                this._currentUser.next(user)
                this.cartService.updateCurrentCart();
            });
    }

    setDefaultLanguage(lang: string) {

        this.http.post<UserData>(App.API.userLanguage, {lang})
            .toPromise()
            .then(user => {
                this._currentUser.next(user)
                this.cartService.updateCurrentCart();
            });
    }
}