import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {DefaultLayoutComponent} from "./layouts/default-layout.component";
import {RoutingModule} from "./routing/routing.module";
import {HeaderComponent} from './shared/components/header/header.component';
import {HomePageComponent} from './pages/home-page/home-page.component';
import {PagesModule} from "./pages/pages.module";
import {CoreModule} from "./core/core.module";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { FooterComponent } from './shared/components/footer/footer.component';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import { LoginLayoutComponent } from './layouts/login-layout/login-layout.component';
import { LoginComponent } from './shared/components/login/login.component';
import {ComponentsModule} from "./shared/components/components.module";

@NgModule({
    declarations: [
        DefaultLayoutComponent,
        LoginLayoutComponent,
        AppComponent,
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        PagesModule,
        RoutingModule,
        CoreModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
        ComponentsModule
    ],
    providers: [],
    exports: [
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http, './app/assets/i18n/');
}