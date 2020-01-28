import {NgModule, Optional, SkipSelf} from '@angular/core';
import {LanguageService} from "./service/language.service";
import {StoreService} from "./service/store.service";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";
import {AuthService} from "./service/auth.service";
import {AuthGuardService} from "./service/authguard.service";
import {UserService} from "./service/user.service";
import {CookieService} from "ngx-cookie-service";
import {CategoryService} from "./service/category.service";
import {SearchService} from "./service/search.service";
import {ProductService} from "./service/product.service";
import {CartService} from "./service/cart.service";


@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        HttpClientModule,
        RouterModule,
    ],
    exports: [
        HttpClientModule
    ],
    providers: [
        CookieService,
        AuthService,
        AuthGuardService,
        UserService,
        LanguageService,
        CategoryService,
        StoreService,
        ProductService,
        CartService,
        SearchService]
})
export class CoreModule {
    // Prevent reimport of the CoreModule
    constructor( @Optional() @SkipSelf() parentModule: CoreModule) {
        if (parentModule) {
            throw new Error('CoreModule is already loaded. Import it in the AppModule only');
        }
    }
}
