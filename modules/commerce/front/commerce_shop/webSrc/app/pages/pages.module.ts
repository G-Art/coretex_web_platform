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

@NgModule({
    declarations: [
        LoginPageComponent,
        AccountPageComponent,
        RegisterPageComponent,
        HomePageComponent,
        ProductListPageComponent
    ],
    imports: [
        FragmentsModule,
        BrowserModule,
        RoutingModule,
        HttpClientModule,
        CoreModule,
        ComponentsModule
    ],
    exports: [
        FragmentsModule
    ],
    providers: []
})
export class PagesModule {
}
