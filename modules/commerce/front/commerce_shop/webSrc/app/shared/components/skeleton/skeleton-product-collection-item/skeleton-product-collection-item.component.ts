import {Component, HostBinding, Input, OnInit} from '@angular/core';
import {fadeInAnimation} from "../../../../core/animation/fadeInAnimation.animation";

@Component({
  animations : [fadeInAnimation],
  host: { '[@fadeInAnimation]': '' },
  selector: 'app-skeleton-product-collection-item',
  templateUrl: './skeleton-product-collection-item.component.html',
  styleUrls: ['./skeleton-product-collection-item.component.scss']
})
export class SkeletonProductCollectionItemComponent implements OnInit {

  @HostBinding('class')
  @Input()
  columnsClass: string = 'col-lg-4';

  @Input()
  itemView: string = 'grid';

  constructor() { }

  ngOnInit(): void {
  }

}
