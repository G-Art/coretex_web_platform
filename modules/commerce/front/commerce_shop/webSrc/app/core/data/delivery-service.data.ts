import {AbstractData} from "./abstract.data";
import {StoreData} from "./store.data";

export class DeliveryServiceData extends AbstractData {
    code:string;
    name:Map<string, string>;
    active:boolean;
    type:string;
    stores:StoreData[];
}
