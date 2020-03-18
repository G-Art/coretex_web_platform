import { Component, OnInit } from '@angular/core';
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";

@Component({
  animations : [fadeInAnimation],
  host: { '[@fadeInAnimation]': '' },
  selector: 'app-checkout-page',
  templateUrl: './checkout-page.component.html',
  styleUrls: ['./checkout-page.component.scss']
})
export class CheckoutPageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
