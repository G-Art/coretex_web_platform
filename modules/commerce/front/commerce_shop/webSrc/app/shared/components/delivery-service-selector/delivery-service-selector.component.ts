import {Component, Input, OnInit} from '@angular/core';
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";
import {CartData} from "../../../core/data/cart.data";
import {DeliveryServiceData} from "../../../core/data/delivery-service.data";
import {DeliveryServiceService} from "../../../core/service/delivery-service.service";
import {Observable} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

@Component({
    animations: [fadeInAnimation],
    host: {'[@fadeInAnimation]': ''},
    selector: 'app-delivery-service-selector',
    templateUrl: './delivery-service-selector.component.html',
    styleUrls: ['./delivery-service-selector.component.scss']
})
export class DeliveryServiceSelectorComponent implements OnInit {

    @Input()
    cart: CartData;
    selectedDeliveryService: DeliveryServiceData;

    deliveryServices: Observable<DeliveryServiceData[]>;

    constructor(private deliveryServiceService: DeliveryServiceService, public translate: TranslateService) {

    }

    ngOnInit(): void {
        this.deliveryServices = this.deliveryServiceService.getForCart(this.cart)
    }

    selection(ds: DeliveryServiceData) {
        if (this.selectedDeliveryService && this.selectedDeliveryService.uuid == ds.uuid) {
            this.selectedDeliveryService = null;
        } else {
            this.selectedDeliveryService = ds;
        }
    }

    isOpen(ds: DeliveryServiceData) {
        return this.selectedDeliveryService && this.selectedDeliveryService.uuid == ds.uuid
    }
}
