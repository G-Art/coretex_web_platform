import {AbstractData} from "./abstract.data";
import {ImgData} from "./img.data";
import {ProductVariantData} from "./product-variant.data";
import {CartEntryData} from "./cart-entry.data";
export class MiniCartData extends AbstractData {

    entries?:CartEntryData[];

    productCount: number = 0;

    total:string;


}