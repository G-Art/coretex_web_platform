import {NgModule, Optional, SkipSelf} from '@angular/core';
import {LanguageService} from "./service/language.service";
import {StoreService} from "./service/store.service";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";


@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        HttpClientModule,
        RouterModule],
    exports: [
        HttpClientModule
    ],
    providers: [
        LanguageService,
        StoreService]
})
export class CoreModule {
    // Prevent reimport of the CoreModule
    constructor( @Optional() @SkipSelf() parentModule: CoreModule) {
        if (parentModule) {
            throw new Error('CoreModule is already loaded. Import it in the AppModule only');
        }
    }
}
