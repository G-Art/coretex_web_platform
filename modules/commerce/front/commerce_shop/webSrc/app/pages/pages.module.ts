import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RoutingModule} from "../routing/routing.module";
import {FragmentsModule} from "../shared/fragments/fragments.module";
import {CoreModule} from "../core/core.module";
import {HttpClientModule} from "@angular/common/http";
import { LoginPageComponent } from './login-page/login-page.component';
import { AccountPageComponent } from './account-page/account-page.component';
import { RegisterPageComponent } from './register-page/register-page.component';
import {HomePageComponent} from "./home-page/home-page.component";
import {ComponentsModule} from "../shared/components/components.module";
import { ProductListPageComponent } from './product-list-page/product-list-page.component';
import { ProductDetailPageComponent } from './product-detail-page/product-detail-page.component';
import {DirectivesModule} from "../shared/directives/directives.module";
import {FormsModule} from "@angular/forms";
import { CartPageComponent } from './cart-page/cart-page.component';
import {NgxSkeletonLoaderModule} from "ngx-skeleton-loader";
import { CheckoutPageComponent } from './checkout-page/checkout-page.component';

@NgModule({
    declarations: [
        LoginPageComponent,
        AccountPageComponent,
        RegisterPageComponent,
        HomePageComponent,
        ProductListPageComponent,
        ProductDetailPageComponent,
        CartPageComponent,
        CheckoutPageComponent
    ],
    imports: [
        FragmentsModule,
        BrowserModule,
        RoutingModule,
        HttpClientModule,
        CoreModule,
        ComponentsModule,
        DirectivesModule,
        FormsModule,
        NgxSkeletonLoaderModule
    ],
    exports: [
        FragmentsModule
    ],
    providers: []
})
export class PagesModule {
}
