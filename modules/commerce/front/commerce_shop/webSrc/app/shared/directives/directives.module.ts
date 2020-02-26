import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SlickSliderDirective} from './slick-slider.directive';


@NgModule({
    declarations: [
        SlickSliderDirective],
    imports: [
        CommonModule
    ],
    exports: [
        SlickSliderDirective
    ]

})
export class DirectivesModule {
}
