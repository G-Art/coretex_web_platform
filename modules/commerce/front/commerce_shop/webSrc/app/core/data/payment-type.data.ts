import {AbstractData} from './abstract.data';

export class PaymentType extends AbstractData {
    type:string;
    code:string;
    name:Map<string, string>;
}
