import {ProductData} from "./product.data";
import {BreadcrumbData} from "./breadcrumb.data";

export class SearchResult {

    page:number;
    totalPages:number;
    count:number;
    totalCount:number;

    breadcrumb:BreadcrumbData[];

    products:ProductData[]

}