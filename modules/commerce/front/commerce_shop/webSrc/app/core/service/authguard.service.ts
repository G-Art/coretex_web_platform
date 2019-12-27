import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthService} from './auth.service';
import {environment} from "../../../environments/environment";

@Injectable()
export class AuthGuardService implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url: string = state.url;
         return this.checkLogin(url);
    }

    checkLogin(url: string): boolean {
        if (this.authService.isAuthenticated()) { return true; }

        this.authService.redirectUrl = url;
        this.router.navigate([`${environment.routes.loginUrl}`]);
        return false;
    }
}