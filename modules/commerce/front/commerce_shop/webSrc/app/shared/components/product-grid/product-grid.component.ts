import {Component, ContentChildren, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {ProductCollectionItemComponent} from "../product-collection-item/product-collection-item.component";

declare var $: any;

@Component({
    selector: 'app-product-grid',
    templateUrl: './product-grid.component.html',
    styleUrls: ['./product-grid.component.scss']
})
export class ProductGridComponent implements OnInit {

    viewMode: string = 'grid';

    gridColumns: number = 3;
    gridClassName: string = 'three-column';

    @ViewChild('gridViewChangeTrigger', {static: false})
    gridViewChangeTrigger: ElementRef;

    @ViewChild('gridViewChanger', {static: false})
    gridViewChanger: ElementRef;

    @ContentChildren(ProductCollectionItemComponent)
    productCollectionItemComponents: QueryList<ProductCollectionItemComponent>;


    gridMap = [
        {
            columns: 2,
            className: 'two-column',
            columnsClass: 'col-lg-6'
        },
        {
            columns: 3,
            className: 'three-column',
            columnsClass: 'col-lg-4'
        },
        {
            columns: 4,
            className: 'four-column',
            columnsClass: 'col-lg-3'
        },
    ];

    constructor() {
    }

    setViewMode(mode: string) {
        this.viewMode = mode;
        this.productCollectionItemComponents.forEach(c => c.setItemView(mode))
    }


    setGridColumns(value: number) {
        this.gridColumns = value;
        this.gridClassName = this.gridMap.find(({columns}) => columns === value).className;

        this.productCollectionItemComponents.forEach(c => c.setColumns(this.gridMap.find(({columns}) => columns === value).columnsClass))
    }

    ngOnInit() {

    }

    triggerGridChanger() {
        let classList = this.gridViewChanger.nativeElement.classList;
        if(classList.contains('active')){
            classList.remove('active')
        }else {
            classList.add('active')
        }

    }
}
