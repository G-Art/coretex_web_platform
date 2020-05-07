import {AbstractData} from "./abstract.data";

export class AddressData extends AbstractData {
    addressLine1?:string;
    addressLine2?:string;
    email?:string;
    city?:string;
    postalCode?:string;
    state?:string;
    phone?:string;
    lastName?:string;
    firstName?:string;
    longitude?:string;
    latitude?:string;
    additionalInfo?:Map<string, any>
}
