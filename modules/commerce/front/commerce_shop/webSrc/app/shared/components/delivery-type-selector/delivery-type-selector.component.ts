import {Component, Input, OnInit} from '@angular/core';
import {DeliveryServiceData} from "../../../core/data/delivery-service.data";
import {DeliveryTypeService} from "../../../core/service/delivery-type.service";
import {DeliveryTypeData} from "../../../core/data/delivery-type.data";
import {DeliveryServiceService} from "../../../core/service/delivery-service.service";
import {TranslateService} from "@ngx-translate/core";
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";
import {Observable} from "rxjs";

@Component({
  animations: [fadeInAnimation],
  host: {'[@fadeInAnimation]': ''},
  selector: 'app-delivery-type-selector',
  templateUrl: './delivery-type-selector.component.html',
  styleUrls: ['./delivery-type-selector.component.scss']
})
export class DeliveryTypeSelectorComponent implements OnInit {

  private _deliveryService:DeliveryServiceData;

  deliveryTypes: Observable<DeliveryTypeData[]>;

  constructor(private deliveryTypeService: DeliveryTypeService, public translate: TranslateService) { }

  ngOnInit(): void {
  }

  get deliveryService(): DeliveryServiceData {
    return this._deliveryService;
  }

  @Input()
  set deliveryService(value: DeliveryServiceData) {
    this._deliveryService = value;
    this.deliveryTypes = this.deliveryTypeService.getDeliveryTypeForService(this._deliveryService);
  }

}
