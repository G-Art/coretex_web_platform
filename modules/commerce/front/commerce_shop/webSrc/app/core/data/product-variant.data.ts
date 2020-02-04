import {AbstractData} from "./abstract.data";
import {ImgData} from "./img.data";
import {ProductData} from "./product.data";
export class ProductVariantData extends ProductData {

    colorCssCode?:string;
    size?:string;
}