import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {RegisterForm} from '../data/register-form.data';
import {UserData} from '../data/user.data';
import {map, share} from 'rxjs/operators';
import {CartService} from './cart.service';

@Injectable()
export class UserService implements OnInit {
    private _currentUser: BehaviorSubject<UserData>;
    currentUser: Observable<UserData>;

    constructor(private http: HttpClient, private cartService: CartService) {
        this._currentUser = new BehaviorSubject<UserData>(undefined);
        this.currentUser = this._currentUser.asObservable();
    }

    ngOnInit(): void {
    }

    authenticate(name: string, password: string): Observable<any> {
        return this.http
            .post(`${environment.baseApiUrl}/login`, {name, password})
            .pipe(share());
    }

    logout(): Observable<any> {
        return this.http
            .get(`${environment.baseApiUrl}/logout`)
            .pipe(share());
    }

    register(registerForm: RegisterForm): Observable<any> {
        return this.http
            .post(`${environment.baseApiUrl}/register`, registerForm)
            .pipe(share());
    }

    updateCurrentUser(): void {
        this.http.get<UserData>(`${environment.baseApiUrl}/user/current`)
            .subscribe(user => {
                this._currentUser.next(user)
                this.cartService.updateCurrentCart();
            });
    }

}