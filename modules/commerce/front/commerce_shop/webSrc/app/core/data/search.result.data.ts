import {ProductData} from "./product.data";
import {BreadcrumbData} from "./breadcrumb.data";
import {FacetData} from './facet-data.data';

export class SearchResult {

    page:number;
    totalPages:number;
    count:number;
    totalCount:number;

    breadcrumb:BreadcrumbData[];

    products:ProductData[]

    facets?:FacetData[]

}