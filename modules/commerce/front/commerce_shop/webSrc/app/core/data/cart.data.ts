import {AbstractData} from "./abstract.data";
import {CartEntryData} from "./cart-entry.data";
import {DeliveryTypeData} from "./delivery-type.data";
import {UserData} from './user.data';
export class CartData extends AbstractData {

    entries?:CartEntryData[];

    productCount: number = 0;

    total:string;

    deliveryType?:DeliveryTypeData;
    address?:AbstractData;
    user?:UserData;

}