import {
    Component,
    ComponentFactoryResolver, Injector,
    Input,
    OnInit, Type,
    ViewChild,
    ViewContainerRef
} from '@angular/core';
import {DeliveryServiceData} from "../../../core/data/delivery-service.data";
import {DeliveryTypeService} from "../../../core/service/delivery-type.service";
import {DeliveryTypeData} from "../../../core/data/delivery-type.data";
import {TranslateService} from "@ngx-translate/core";
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";
import {CartService} from "../../../core/service/cart.service";
import {NewPostDeliveryTypeComponent} from "../delivery-types/new-post-delivery-type/new-post-delivery-type.component";
import {AbstractDeliveryType} from "../delivery-types/abstract-delivery-type";
import {CartData} from '../../../core/data/cart.data';

@Component({
    animations: [fadeInAnimation],
    host: {'[@fadeInAnimation]': ''},
    selector: 'app-delivery-type-selector',
    templateUrl: './delivery-type-selector.component.html',
    styleUrls: ['./delivery-type-selector.component.scss']
})
export class DeliveryTypeSelectorComponent implements OnInit {

    @ViewChild("deliveryTypeData", {static: true, read: ViewContainerRef}) deliveryTypeData: ViewContainerRef;

    private _deliveryService: DeliveryServiceData;

    private deliveryTypeMap: Map<string, Type<AbstractDeliveryType>> = new Map<string, Type<any>>(
        [
            ["NewPostDeliveryType", NewPostDeliveryTypeComponent]
        ]
    );

    @Input()
    cart:CartData;
    deliveryTypes: DeliveryTypeData[];
    selectedType: DeliveryTypeData;

    constructor(private deliveryTypeService: DeliveryTypeService,
                private cartService: CartService,
                private componentFactoryResolver: ComponentFactoryResolver,
                public translate: TranslateService) {
    }

    ngOnInit(): void {
        if (this.cart.deliveryType && (this.selectedType == null || this.selectedType.uuid !== this.cart.deliveryType.uuid)) {
            if (!this.selectedType) {
                this.selectedType = this.cart.deliveryType
                this.addDeliveryTypeForm()
            }
        }
    }

    get deliveryService(): DeliveryServiceData {
        return this._deliveryService;
    }

    @Input()
    set deliveryService(value: DeliveryServiceData) {
        this._deliveryService = value;
        this.deliveryTypeService.getDeliveryTypeForService(this._deliveryService)
            .subscribe(next => {
                this.deliveryTypes = next;
                if(this.deliveryTypes && !this.selectedType){
                    this.selectedType = this.deliveryTypes.find(dt => dt);
                    this.cartService.addDeliveryType(this.selectedType, () => {
                        this.addDeliveryTypeForm()
                    });
                }
            });
    }

    selectDeliveryType(value: string) {
        this.selectedType = this.deliveryTypes.find(dt => dt.code === value);
        this.cartService.addDeliveryType(this.selectedType, () => {
            this.addDeliveryTypeForm()
        });
    }

    private addDeliveryTypeForm() {
        this.deliveryTypeData.clear();
        let componentFactory = this.componentFactoryResolver.resolveComponentFactory(this.deliveryTypeMap.get(this.selectedType.type));
        let componentRef = this.deliveryTypeData.createComponent(componentFactory);
        let instance = componentRef.instance;
        instance.deliveryType = this.selectedType;
        instance.cart = this.cart;
    }
}
