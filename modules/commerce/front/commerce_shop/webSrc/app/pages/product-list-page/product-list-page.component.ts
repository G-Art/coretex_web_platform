import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

declare var $: any;

@Component({
    selector: 'app-product-list-page',
    templateUrl: './product-list-page.component.html',
    styleUrls: ['./product-list-page.component.scss']
})
export class ProductListPageComponent implements OnInit {

    type: string = 'category';

    query: string;

    constructor(private activatedRoute: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit() {
        this.init();
        this.activatedRoute.data.subscribe(data => {
            this.type = data.type;
        });

        if (this.type === 'category') {
            this.query = this.activatedRoute
                .snapshot
                .params.code;
        } else {
            this.activatedRoute
                .queryParams
                .subscribe(params => this.query = params['q']);
        }

        console.log(`${this.type} | ${this.query}`)

    }

    private init() {


    }

}
