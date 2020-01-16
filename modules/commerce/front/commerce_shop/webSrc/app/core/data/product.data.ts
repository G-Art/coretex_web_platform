import {AbstractData} from "./abstract.data";
import {ImgData} from "./img.data";
export class ProductData extends AbstractData {
    code:string;

    title:string;

    images:ImgData[];

    price:string;
    priceDiscount:string;

    hotBadge:boolean;
    discountBadgeInfo:string;

    description:string;
}