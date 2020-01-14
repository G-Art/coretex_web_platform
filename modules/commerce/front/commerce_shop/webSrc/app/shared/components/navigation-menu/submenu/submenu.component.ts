import {Component, Input, OnInit} from '@angular/core';
import {CategoryData} from "../../../../core/data/category.data";

@Component({
  selector: 'app-submenu',
  templateUrl: './submenu.component.html',
  styleUrls: ['./submenu.component.scss']
})
export class SubmenuComponent implements OnInit {

  @Input()
  subMenuClasses: string = 'submenu submenu--column-1';

  @Input()
  categoryData:CategoryData[];

  constructor() { }

  ngOnInit() {
  }

}
