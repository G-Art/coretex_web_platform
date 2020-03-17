import { Component, OnInit } from '@angular/core';
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";

@Component({
  animations : [fadeInAnimation],
  host: { '[@fadeInAnimation]': '' },
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
