import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DefaultLayoutComponent} from "../layouts/default-layout.component";
import {HomePageComponent} from "../pages/home-page/home-page.component";
import {LoginLayoutComponent} from "../layouts/login-layout/login-layout.component";
import {LoginPageComponent} from "../pages/login-page/login-page.component";
import {AccountPageComponent} from "../pages/account-page/account-page.component";
import {AuthGuardService} from "../core/service/authguard.service";
import {RegisterPageComponent} from "../pages/register-page/register-page.component";
import {ProductListPageComponent} from "../pages/product-list-page/product-list-page.component";


const routes: Routes = [
    {
        path: '',
        component: DefaultLayoutComponent,
        children: [
            {
                path: '',
                pathMatch: 'full',
                component: HomePageComponent
            },
            {
                path: 'category/:code',
                component: ProductListPageComponent,
                data: {type: 'category'}
            },
            {
                path: 'search',
                component: ProductListPageComponent,
                data: {type: 'search'}
            },
            {
                path: 'account',
                canActivate: [AuthGuardService],
                component: AccountPageComponent
            }
        ]

    },
    {
        path: '',
        component: LoginLayoutComponent,
        children: [
            {
                path: 'login',
                component: LoginPageComponent
            },
            {
                path: 'register',
                component: RegisterPageComponent
            }
        ]
    },
    {path: '**', redirectTo: ''}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]

})

export class RoutingModule {
}