import {Component, OnInit} from '@angular/core';
import {SearchService} from '../../core/service/search.service';
import {SearchResult} from '../../core/data/search.result.data';
import {ActivatedRoute, Router} from '@angular/router';
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

    facets: Map<string, string[]> = new Map<string, string[]>()

    type: string = 'category';

    showSkeleton: boolean = false;

    searchResult: SearchResult;

    constructor(private search: SearchService,
                private activatedRoute: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit() {
        this.showSkeleton = true;
        this.onParamChange.subscribe(value => {
            this.showSkeleton = true;
            this.search.searchCategory(this.categoryCode, this.page, this.facets);
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
                        this.facets.clear()
                        Object.keys(results.query)
                            .filter(f => f.startsWith('f('))
                            .forEach(f => {

                                let facetName = f.substring(f.indexOf('(') + 1, f.lastIndexOf(')'));
                                let values = results.query[f].split(',')
                                this.facets.set(facetName, values)
                            })
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


    addFacet(f: any) {
        let name = f.name;
        let value = f.value;

        if (this.facets.has(name)) {

            let strings = this.facets.get(name);
            const index = strings.indexOf(value);
            if (index == -1) {
                strings.push(value);
            }
            this.facets.set(name, strings);
        } else {
            this.facets.set(name, [value]);
        }

        this.updateUrl()
        this.onParamChange.next()
    }

    removeFacet(f: any) {
        let name = f.name;
        let value = f.value;

        if (this.facets.has(name)) {
            let strings = this.facets.get(name);
            const index = strings.indexOf(value);
            if (index > -1) {
                strings.splice(index);
            }

            this.updateUrl()

            this.onParamChange.next()
        }
    }

    updateUrl() {

        let params = {};
        let snapshot = this.activatedRoute.snapshot;
        snapshot.queryParamMap
            .keys
            .forEach(key => {
                params[key] = null;
            })
        if (this.page > 0) {
            params['page'] = this.page;
        }

        this.facets.forEach((values, key) => {
            if(values != null && values.length > 0){
                params[`f(${key})`] = values.join(',');
            }
        })

        this.router.navigate(['.'], {
            relativeTo: this.activatedRoute,
            queryParams: params,
            replaceUrl:true
        });
    }
}
