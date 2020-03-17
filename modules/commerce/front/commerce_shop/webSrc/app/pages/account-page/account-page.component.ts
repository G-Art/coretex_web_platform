import { Component, OnInit } from '@angular/core';
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";

@Component({
  animations : [fadeInAnimation],
  host: { '[@fadeInAnimation]': '' },
  selector: 'app-account-page',
  templateUrl: './account-page.component.html',
  styleUrls: ['./account-page.component.scss']
})
export class AccountPageComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
