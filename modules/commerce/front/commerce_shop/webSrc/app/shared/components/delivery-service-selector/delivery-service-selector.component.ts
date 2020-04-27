import {Component, Input, OnInit} from '@angular/core';
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";
import {CartData} from "../../../core/data/cart.data";
import {DeliveryServiceData} from "../../../core/data/delivery-service.data";
import {DeliveryServiceService} from "../../../core/service/delivery-service.service";
import {Observable} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {CartService} from "../../../core/service/cart.service";

@Component({
    animations: [fadeInAnimation],
    host: {'[@fadeInAnimation]': ''},
    selector: 'app-delivery-service-selector',
    templateUrl: './delivery-service-selector.component.html',
    styleUrls: ['./delivery-service-selector.component.scss']
})
export class DeliveryServiceSelectorComponent implements OnInit {

    @Input()
    show:boolean = false;

    @Input()
    cart: CartData;
    selectionMap : Map<string, boolean> = new Map<string, boolean>();

    selectedDeliveryService: DeliveryServiceData = null;

    deliveryServices: DeliveryServiceData[];

    constructor(private deliveryServiceService: DeliveryServiceService,
                private cartService: CartService,
                public translate: TranslateService) {

    }

    ngOnInit(): void {
         this.deliveryServiceService.getForCart(this.cart)
            .subscribe(next => {
                this.deliveryServices = next;
                this.deliveryServices.forEach(value => {
                    this.selectionMap[value.code] = false;
                });
                if (this.cart.deliveryType) {
                    let deliveryServiceData = next.find(ds => ds.uuid === this.cart.deliveryType.deliveryService);
                    this.selectionMap[deliveryServiceData.code] = true;
                    this.selection(deliveryServiceData);
                }
            });
    }

    selection(ds: DeliveryServiceData) {
        if (this.selectedDeliveryService && this.selectedDeliveryService.uuid === ds.uuid) {
            if (this.cart.deliveryType && this.cart.deliveryType.deliveryService === ds.uuid) {
                this.cartService.addDeliveryType(null);
                this.selectedDeliveryService = null;
            }
        } else {
            this.selectedDeliveryService = ds;
        }
    }

    selected(ds: DeliveryServiceData): boolean {
        return this.selectedDeliveryService !== null && this.selectedDeliveryService.uuid === ds.uuid;
    }
}
