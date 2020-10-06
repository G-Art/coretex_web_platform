import {AddressData} from './address.data';
import {LanguageData} from './language.data';

export interface UserData {
    email:string;
    firstName:string;
    lastName:string;
    active: boolean;

    billing?:AddressData;
    delivery?:AddressData;
    language?:LanguageData;
    //
    // private AddressData billing;
    // private AddressData delivery;
}