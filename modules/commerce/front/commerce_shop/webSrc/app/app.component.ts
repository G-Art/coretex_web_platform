import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

import {filter, map, mergeMap} from 'rxjs/operators';
import {TranslateService} from '@ngx-translate/core';
import {StoreService} from './core/service/store.service';
import {LanguageData} from './core/data/language.data';
import {AuthService} from './core/service/auth.service';
import {UserService} from './core/service/user.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {
    defaultStoreLanguage: LanguageData;
    pageTitle: string;

    constructor(private router: Router,
                private storeService: StoreService,
                private activatedRoute: ActivatedRoute,
                private titleService: Title,) {

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
