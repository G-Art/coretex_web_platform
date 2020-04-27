import {HostBinding, Input} from '@angular/core';
import {DeliveryTypeData} from "../../../core/data/delivery-type.data";
import {CartData} from '../../../core/data/cart.data';

export abstract class AbstractDeliveryType {
    @HostBinding('class')
    columnsClass: string = 'col-12';

    @Input()
    deliveryType: DeliveryTypeData;
    @Input()
    cart:CartData;

}
