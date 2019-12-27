import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";
import {StoreService} from "../../../core/service/store.service";
import {StoreData} from "../../../core/data/store.data";
import {LanguageService} from "../../../core/service/language.service";
import {LanguageData} from "../../../core/data/language.data";

@Component({
    selector: 'app-language-selector',
    templateUrl: './language-selector.component.html',
    styleUrls: ['./language-selector.component.css']
})
export class LanguageSelectorComponent implements OnInit {

    selectedLanguage: LanguageData;
    allLanguage: LanguageData[];
    languages: LanguageData[];

    constructor(private router: Router,
                private translate: TranslateService,
                private storeService: StoreService,
                private langService: LanguageService) {
    }

    ngOnInit() {
        let currentLang = this.translate.currentLang;

        this.storeService.getCurrentStore()
            .subscribe((store: StoreData) => {
                let storeUuid = store.uuid;

                this.langService.getStorageLanguages(storeUuid)
                    .subscribe( (langs:LanguageData[]) => {
                    this.allLanguage = langs;
                    this.selectedLanguage = langs.find(({ isocode }) => isocode === currentLang);
                    this.languages = langs.filter(({ isocode }) => isocode !== currentLang);
                });

            });
    }

    useLanguage(langIso: string) {
        if (this.translate.currentLang !== langIso) {
            this.selectedLanguage = this.allLanguage.find(({isocode}) => isocode === langIso);
            this.languages = this.allLanguage.filter(({ isocode }) => isocode !== langIso);
            this.translate.use(langIso)
        }
    }
}
