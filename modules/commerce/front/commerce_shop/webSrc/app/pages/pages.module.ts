import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RoutingModule} from "../routing/routing.module";
import {FragmentsModule} from "../shared/fragments/fragments.module";
import {CoreModule} from "../core/core.module";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
    declarations: [],
    imports: [
        FragmentsModule,
        BrowserModule,
        RoutingModule,
        HttpClientModule,
        CoreModule
    ],
    exports: [
        FragmentsModule
    ],
    providers: []
})
export class PagesModule {
}
