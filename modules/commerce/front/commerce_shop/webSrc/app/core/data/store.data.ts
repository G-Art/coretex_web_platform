import {AbstractData} from "./abstract.data";
import {AddressData} from "./address.data";
import {LanguageData} from "./language.data";

export class StoreData extends AbstractData {
    code?: string;
    phone?: string;
    domainName?: string;
    storeEmail?: string;
    dateBusinessSince?:string;
    name?: string;

    address?:AddressData;
    defaultLanguage?:LanguageData;

}
