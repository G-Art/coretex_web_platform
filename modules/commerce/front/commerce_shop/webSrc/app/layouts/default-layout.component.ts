import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {StoreData} from '../core/data/store.data';
import {StoreService} from '../core/service/store.service';
import {TranslateService} from '@ngx-translate/core';
import {fadeInAnimation} from '../core/animation/fadeInAnimation.animation';
import {FormControl, FormGroup} from '@angular/forms';

declare var $: any;

@Component({
    animations: [fadeInAnimation],
    host: {'[@fadeInAnimation]': ''},
    selector: 'app-default-layout',
    templateUrl: './default-layout.component.html',
    styleUrls: ['./default-layout.component.scss']
})
export class DefaultLayoutComponent implements OnInit {

    mobileMenuOpen: boolean = false;

    public currentStore: StoreData;

    constructor(private storeService: StoreService, private translate: TranslateService) {
        this.storeService.getCurrentStore()
            .subscribe((store: StoreData) => {
                this.currentStore = store;
                if (this.translate.currentLang !== this.currentStore.defaultLanguage.isocode) {
                    this.translate.use(this.currentStore.defaultLanguage.isocode)
                }
            });
    }

    ngOnInit(): void {
    }

    scrollTop() {
        $('html,body').animate({
            scrollTop: 0
        }, 2000);
    }


    @HostListener('window:scroll', ['$event'])
    scrollHandler(event) {
        let $window = $(window);
        let scroll = $window.scrollTop();

        if (scroll >= 400) {
            $('.scroll-top').fadeIn();
        } else {
            $('.scroll-top').fadeOut();
        }
    }

    toggleMobileMenuEvent($event: any) {
        this.mobileMenuOpen = !this.mobileMenuOpen;
    }
}
