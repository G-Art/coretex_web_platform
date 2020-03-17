import {Component, Input, OnInit} from '@angular/core';
import {SearchResult} from "../../../../core/data/search.result.data";
import {fadeInAnimation} from "../../../../core/animation/fadeInAnimation.animation";

declare var $: any;

@Component({
    animations : [fadeInAnimation],
    host: { '[@fadeInAnimation]': '' },
    selector: 'app-skeleton-sidebar',
    templateUrl: './skeleton-sidebar.component.html',
    styleUrls: ['./skeleton-sidebar.component.scss']
})
export class SkeletonSidebarComponent implements OnInit {


    constructor() {
    }

    ngOnInit() {
    }

}
