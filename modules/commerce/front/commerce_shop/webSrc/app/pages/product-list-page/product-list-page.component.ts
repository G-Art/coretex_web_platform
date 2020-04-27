import {Component, OnInit} from '@angular/core';
import {SearchService} from '../../core/service/search.service';
import {SearchResult} from '../../core/data/search.result.data';
import {ActivatedRoute} from '@angular/router';
import {combineLatest, Subject} from 'rxjs';
import {fadeInAnimation} from '../../core/animation/fadeInAnimation.animation';
import {map} from 'rxjs/operators';

declare var $: any;

@Component({
    animations: [fadeInAnimation],
    host: {'[@fadeInAnimation]': ''},
    selector: 'app-product-list-page',
    templateUrl: './product-list-page.component.html',
    styleUrls: ['./product-list-page.component.scss']
})
export class ProductListPageComponent implements OnInit {

    private categoryCode: string;
    private page: number = 0;

    private onParamChange = new Subject<void>();

    type: string = 'category';

    showSkeleton: boolean = false;

    searchResult: SearchResult;

    constructor(private search: SearchService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.showSkeleton = true;
        this.onParamChange.subscribe(value => {
            this.showSkeleton = true;
            this.search.searchCategory(this.categoryCode, this.page);
        });
        this.search
            .searchResult
            .subscribe(next => {
                this.searchResult = next;
                this.showSkeleton = false;
            });

        this.activatedRoute.data.subscribe(data => {
            this.type = data.type;
            if (this.type === 'category') {
                combineLatest([this.activatedRoute.params, this.activatedRoute.queryParams])
                    .pipe(map(results => ({params: results[0].code, query: results[1]})))
                    .subscribe(results => {
                        this.categoryCode = results.params;

                        let page = results.query['page'];
                        if (page != null || page != undefined) {
                            this.page = page;
                        } else {
                            this.page = 0;
                        }

                        this.onParamChange.next();
                    });
            } else {
                // this.activatedRoute
                //     .queryParams
                //     .subscribe(params => {
                //         this.search.searchQuery(params['q']).subscribe(data => this.searchResult = data);
                //     });
            }
        });

    }


}
