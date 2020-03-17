import {Component, HostListener, OnInit} from '@angular/core';
import {StoreData} from "../../core/data/store.data";
import {StoreService} from "../../core/service/store.service";
import {TranslateService} from "@ngx-translate/core";
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";

declare var $: any;

@Component({
  animations : [fadeInAnimation],
  host: { '[@fadeInAnimation]': '' },
  selector: 'app-login-layout',
  templateUrl: './login-layout.component.html',
  styleUrls: ['./login-layout.component.css']
})
export class LoginLayoutComponent implements OnInit {

  public currentStore:StoreData;

  constructor(private storeService: StoreService, private translate: TranslateService) {
    this.storeService.getCurrentStore()
        .subscribe((store:StoreData) => {
          this.currentStore = store;
          if(this.translate.currentLang !== this.currentStore.defaultLanguage.isocode){
            this.translate.use(this.currentStore.defaultLanguage.isocode)
          }
        });
  }

  ngOnInit(): void {

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
}