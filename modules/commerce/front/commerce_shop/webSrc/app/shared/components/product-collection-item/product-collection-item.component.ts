import {Component, ElementRef, EventEmitter, HostBinding, Input, OnInit, Output, ViewChild} from '@angular/core';
import {ProductData} from "../../../core/data/product.data";
import {ProductService} from "../../../core/service/product.service";
declare var $: any;

@Component({
  selector: 'app-product-collection-item',
  templateUrl: './product-collection-item.component.html',
  styleUrls: ['./product-collection-item.component.scss']
})
export class ProductCollectionItemComponent implements OnInit {

  @HostBinding('class')
  @Input()
  columnsClass:string = 'col-lg-4';

  @Input()
  itemView:string = 'grid';

  @Input()
  product:ProductData;

  @ViewChild('imageWrapper', {static: false})
  imageWrapper : ElementRef;

  constructor(private productService : ProductService) { }

  ngOnInit() {

  }

  setColumns(value: string){
    this.columnsClass = value;
  }

  setItemView(value: string){
    this.itemView = value
  }

  displayQuickView(){
    this.productService.showQuickViewFor(this.product, this.imageWrapper)
  }
}
