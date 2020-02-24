import {AbstractData} from "./abstract.data";
import {ImgData} from "./img.data";
import {ProductVariantData} from "./product-variant.data";
export class CartEntryData extends AbstractData {

    product:ProductVariantData;
    quantity:number;
    price:number;
}