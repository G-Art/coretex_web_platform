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

    query: string;

    searchResult:SearchResult;

    constructor(private activatedRoute: ActivatedRoute,
                private search: SearchService) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            this.type = data.type;
        });

        if (this.type === 'category') {
            this.query = this.activatedRoute
                .snapshot
                .params.code;

            this.search.searchCategory(this.query).subscribe(data => this.searchResult = data);
        } else {
            this.activatedRoute
                .queryParams
                .subscribe(params => this.query = params['q']);
            this.search.searchQuery(this.query).subscribe(data => this.searchResult = data);
        }

        console.log(`${this.type} | ${this.query}`)

    }


}
