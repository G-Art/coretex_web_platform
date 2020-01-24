import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {SearchService} from "../../core/service/search.service";
import {SearchResult} from "../../core/data/search.result.data";

declare var $: any;

@Component({
    selector: 'app-product-list-page',
    templateUrl: './product-list-page.component.html',
    styleUrls: ['./product-list-page.component.scss']
})
export class ProductListPageComponent implements OnInit {

    type: string = 'category';

    searchResult: SearchResult;

    constructor(private activatedRoute: ActivatedRoute,
                private search: SearchService) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            this.type = data.type;
            if (this.type === 'category') {
                this.activatedRoute.params.subscribe(routeParams => {
                    this.search.searchCategory(routeParams.code).subscribe(data => this.searchResult = data);
                })
            } else {
                this.activatedRoute
                    .queryParams
                    .subscribe(params => {
                        this.search.searchQuery(params['q']).subscribe(data => this.searchResult = data);
                    });
            }
        });



        // console.log(`${this.type} | ${this.query}`)

    }


}
