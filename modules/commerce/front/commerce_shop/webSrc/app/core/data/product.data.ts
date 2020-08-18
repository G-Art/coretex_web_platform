import {AbstractData} from "./abstract.data";
import {ImgData} from "./img.data";
import {ProductVariantData} from "./product-variant.data";
export class ProductData extends AbstractData {
    code:string;

    title:string;
    name:string;

    variants?:ProductVariantData[];

    images:ImgData[];

    price:string;
    priceDiscount:string;

    hotBadge:boolean;
    discountBadgeInfo:string;

    description:string;
    defaultVariantCode:string;
}