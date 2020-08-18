import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {DefaultLayoutComponent} from "./layouts/default-layout.component";
import {RoutingModule} from "./routing/routing.module";
import {PagesModule} from "./pages/pages.module";
import {CoreModule} from "./core/core.module";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {LoginLayoutComponent} from './layouts/login-layout/login-layout.component';
import {ComponentsModule} from "./shared/components/components.module";
import {DirectivesModule} from "./shared/directives/directives.module";
import {NgxSkeletonLoaderModule} from "ngx-skeleton-loader";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AccordionModule} from "ngx-bootstrap/accordion";
import {ReactiveFormsModule} from '@angular/forms';

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
        NgxSkeletonLoaderModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
        ComponentsModule,
        DirectivesModule,
        BrowserAnimationsModule,
        AccordionModule.forRoot(),
        ReactiveFormsModule,
    ],
    providers: [],
    exports: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http, './app/assets/i18n/');
}