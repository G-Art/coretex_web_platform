import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {LoginComponent} from './login/login.component';
import {FragmentsModule} from '../fragments/fragments.module';
import {RoutingModule} from '../../routing/routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RegisterComponent} from './register/register.component';
import {AppEqualValidatorDirective} from './register/app-equal-validator.directive';
import {SidebarComponent} from './sidebar/sidebar.component';
import {ProductGridComponent} from './product-grid/product-grid.component';
import {ProductCollectionItemComponent} from './product-collection-item/product-collection-item.component';
import {NavigationMenuComponent} from './navigation-menu/navigation-menu.component';
import {SubmenuComponent} from './navigation-menu/submenu/submenu.component';
import {ProductQuickViewComponent} from './product-quick-view/product-quick-view.component';
import {MiniCartComponent} from './mini-cart/mini-cart.component';
import {WishlistComponent} from './wishlist/wishlist.component';
import {DirectivesModule} from '../directives/directives.module';
import {SlickSliderComponent} from './slick-slider/slick-slider.component';
import {ProductImageSliderComponent} from './product-image-slider/product-image-slider.component';
import {SkeletonSidebarComponent} from './skeleton/skeleton-sidebar/skeleton-sidebar.component';
import {NgxSkeletonLoaderModule} from 'ngx-skeleton-loader';
import {SkeletonProductCollectionItemComponent} from './skeleton/skeleton-product-collection-item/skeleton-product-collection-item.component';
import {PaginationComponent} from './pagination/pagination.component';
import {DeliveryServiceSelectorComponent} from './delivery-service-selector/delivery-service-selector.component';
import {AccordionModule} from 'ngx-bootstrap/accordion';
import {TranslateModule} from '@ngx-translate/core';
import {DeliveryTypeSelectorComponent} from './delivery-type-selector/delivery-type-selector.component';
import {NewPostDeliveryTypeComponent} from './delivery-types/new-post-delivery-type/new-post-delivery-type.component';
import {TypeaheadModule} from 'ngx-bootstrap/typeahead';
import {PaymentSelectorComponent} from './payment-selector/payment-selector.component';

@NgModule({
    declarations: [
        HeaderComponent,
        FooterComponent,
        LoginComponent,
        RegisterComponent,
        AppEqualValidatorDirective,
        SidebarComponent,
        SkeletonSidebarComponent,
        ProductGridComponent,
        ProductCollectionItemComponent,
        NavigationMenuComponent,
        SubmenuComponent,
        ProductQuickViewComponent,
        MiniCartComponent,
        WishlistComponent,
        SlickSliderComponent,
        ProductImageSliderComponent,
        SkeletonProductCollectionItemComponent,
        PaginationComponent,
        DeliveryServiceSelectorComponent,
        DeliveryTypeSelectorComponent,
        NewPostDeliveryTypeComponent,
        PaymentSelectorComponent
    ],
    imports: [
        DirectivesModule,
        RoutingModule,
        FragmentsModule,
        ReactiveFormsModule,
        CommonModule,
        NgxSkeletonLoaderModule,
        FormsModule,
        TranslateModule,
        AccordionModule.forRoot(),
        TypeaheadModule.forRoot()
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
        ProductQuickViewComponent,
        SlickSliderComponent,
        ProductImageSliderComponent,
        SkeletonSidebarComponent,
        DeliveryServiceSelectorComponent,
        PaymentSelectorComponent
    ],
    entryComponents: [
        ProductImageSliderComponent
    ]

})
export class ComponentsModule {
}
