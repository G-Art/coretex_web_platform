import {AbstractData} from "./abstract.data";

export class DeliveryTypeData extends AbstractData {
    code:string;
    name:Map<string, string>;
    active:boolean;
    type:string;
}
