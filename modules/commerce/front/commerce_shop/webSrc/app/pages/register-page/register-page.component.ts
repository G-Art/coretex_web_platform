import { Component, OnInit } from '@angular/core';
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";

@Component({
  animations : [fadeInAnimation],
  host: { '[@fadeInAnimation]': '' },
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
