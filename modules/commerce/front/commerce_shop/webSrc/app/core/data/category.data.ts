import {AbstractData} from "./abstract.data";

export class CategoryData extends AbstractData {
    code:string;

    name:string;

    visible?:boolean;

    root:boolean;

    children?:CategoryData[];
}
