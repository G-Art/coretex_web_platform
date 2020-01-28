import { Component, OnInit } from '@angular/core';
import {ProductDetailData} from "../../../core/data/product-detail.data";
import {ProductData} from "../../../core/data/product.data";

@Component({
  selector: 'app-mini-cart',
  templateUrl: './mini-cart.component.html',
  styleUrls: ['./mini-cart.component.scss']
})
export class MiniCartComponent implements OnInit {

  entries: ProductData[];

  constructor() { }

  ngOnInit() {
  }

}
