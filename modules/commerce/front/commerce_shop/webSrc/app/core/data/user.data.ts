import {AddressData} from './address.data';

export interface UserData {
    email:string;
    firstName:string;
    lastName:string;
    active: boolean;

    billing?:AddressData;
    delivery?:AddressData;
    // private LocaleData language;
    //
    // private AddressData billing;
    // private AddressData delivery;
}