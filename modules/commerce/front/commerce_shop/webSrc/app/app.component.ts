import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

import {filter, map, mergeMap} from 'rxjs/operators';
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {
    pageTitle: string;

    constructor(private router: Router,
                private activatedRoute: ActivatedRoute,
                private titleService: Title,
                private translate: TranslateService) {
        this.translate.setDefaultLang('en');
        this.translate.use('en')
    }

    ngOnInit() {

        this.router.events.pipe(
            filter(event => event instanceof NavigationEnd),
            map(() => this.activatedRoute),
            map(route => {
                while (route.firstChild) route = route.firstChild;
                return route;
            }),
            filter(route => route.outlet === 'primary'),
            mergeMap(route => route.data)
        ).subscribe(event => {
                this.titleService.setTitle(event['title']);
                this.pageTitle = event['pageTitle'];
            });

    }
}
