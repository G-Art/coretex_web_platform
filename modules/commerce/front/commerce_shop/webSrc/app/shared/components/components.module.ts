import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from "./header/header.component";
import {FooterComponent} from "./footer/footer.component";
import {LoginComponent} from "./login/login.component";
import {FragmentsModule} from "../fragments/fragments.module";
import {RoutingModule} from "../../routing/routing.module";
import {ReactiveFormsModule} from "@angular/forms";
import { RegisterComponent } from './register/register.component';
import { AppEqualValidatorDirective } from './register/app-equal-validator.directive';
import { SidebarComponent } from './sidebar/sidebar.component';
import { ProductGridComponent } from './product-grid/product-grid.component';
import { ProductCollectionItemComponent } from './product-collection-item/product-collection-item.component';
import { NavigationMenuComponent } from './navigation-menu/navigation-menu.component';
import { SubmenuComponent } from './navigation-menu/submenu/submenu.component';
import { ProductQuickViewComponent } from './product-quick-view/product-quick-view.component';


@NgModule({
    declarations: [
        HeaderComponent,
        FooterComponent,
        LoginComponent,
        RegisterComponent,
        AppEqualValidatorDirective,
        SidebarComponent,
        ProductGridComponent,
        ProductCollectionItemComponent,
        NavigationMenuComponent,
        SubmenuComponent,
        ProductQuickViewComponent
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
        RegisterComponent,
        SidebarComponent,
        ProductGridComponent,
        ProductCollectionItemComponent,
        NavigationMenuComponent,
        ProductQuickViewComponent
    ]

})
export class ComponentsModule {
}
