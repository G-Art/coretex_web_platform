import {Component, Input, OnInit} from '@angular/core';
import {StoreData} from "../../../core/data/store.data";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  @Input() store: StoreData;

  constructor() { }

  ngOnInit() {
  }

}
