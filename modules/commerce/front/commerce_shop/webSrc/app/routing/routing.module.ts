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
import {ProductDetailPageComponent} from "../pages/product-detail-page/product-detail-page.component";
import {CartPageComponent} from "../pages/cart-page/cart-page.component";
import {CheckoutPageComponent} from "../pages/checkout-page/checkout-page.component";


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
                runGuardsAndResolvers: 'pathParamsOrQueryParamsChange',
                data: {type: 'category'}
            },
            {
                path: 'cart',
                component: CartPageComponent
            },
            {
                path: 'checkout',
                component: CheckoutPageComponent
            },
            {
                path: 'product/:code',
                component: ProductDetailPageComponent
            },
            {
                path: 'product/:code/v/:vcode',
                component: ProductDetailPageComponent
            },
            {
                path: 'search',
                component: ProductListPageComponent,
                runGuardsAndResolvers: 'pathParamsOrQueryParamsChange',
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