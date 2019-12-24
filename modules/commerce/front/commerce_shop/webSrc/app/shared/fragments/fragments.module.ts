import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {RoutingModule} from "../../routing/routing.module";
import { LanguageSelectorComponent } from './language-selector/language-selector.component';
import { CurrencySelectorComponent } from './currency-selector/currency-selector.component';
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
  declarations: [
    LanguageSelectorComponent,
    CurrencySelectorComponent],
  imports: [
    BrowserModule,
    RoutingModule
  ],
  exports: [
    LanguageSelectorComponent,
    CurrencySelectorComponent,
    TranslateModule
  ],
  providers: []
})
export class FragmentsModule {
}
