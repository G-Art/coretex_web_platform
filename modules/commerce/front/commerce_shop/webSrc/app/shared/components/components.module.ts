import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from "./header/header.component";
import {FooterComponent} from "./footer/footer.component";
import {LoginComponent} from "./login/login.component";
import {FragmentsModule} from "../fragments/fragments.module";
import {RoutingModule} from "../../routing/routing.module";
import {ReactiveFormsModule} from "@angular/forms";
import { RegisterComponent } from './register/register.component';


@NgModule({
    declarations: [
        HeaderComponent,
        FooterComponent,
        LoginComponent,
        RegisterComponent
    ],
    imports: [
        RoutingModule,
        FragmentsModule,
        ReactiveFormsModule,
        CommonModule
    ],
    exports: [
        HeaderComponent,
        FooterComponent,
        LoginComponent,
        RegisterComponent
    ]

})
export class ComponentsModule {
}
