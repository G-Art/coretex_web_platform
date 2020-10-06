import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {StoreService} from '../../../core/service/store.service';
import {StoreData} from '../../../core/data/store.data';
import {LanguageService} from '../../../core/service/language.service';
import {LanguageData} from '../../../core/data/language.data';
import {AuthService} from '../../../core/service/auth.service';
import {UserService} from '../../../core/service/user.service';

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
                private langService: LanguageService,
                private authService: AuthService,
                private userService: UserService) {
    }

    ngOnInit() {
        if (this.translate.currentLang) {
            this.initAllLanguages(this.translate.currentLang);
        }
        this.translate.onLangChange
            .subscribe(lang => {
                const currentLang = lang.lang;
                if (!this.allLanguage) {
                    this.initAllLanguages(currentLang);
                }
            })

        this.userService
            .currentUser
            .subscribe(user => {
                if (user.language) {
                    if (this.selectedLanguage !== user.language) {
                        this.selectedLanguage = user.language;
                        this.translate.use(this.selectedLanguage.isocode)
                    }
                }
            })

        this.storeService.getCurrentStore()
            .subscribe(store => {
                this.selectedLanguage = store.defaultLanguage;
                if (!this.translate.currentLang) {
                    const userLang = navigator.language.split('-')[0];
                    if (userLang !== this.selectedLanguage.isocode) {
                        this.translate.setDefaultLang(userLang);
                        this.translate.use(userLang)
                    } else {
                        this.translate.setDefaultLang(this.selectedLanguage.isocode);
                        this.translate.use(this.selectedLanguage.isocode);
                    }
                }
            });
    }

    private initAllLanguages(currentLang: string) {
        this.storeService.getCurrentStore()
            .subscribe((store: StoreData) => {
                const storeUuid = store.uuid;

                this.langService.getStorageLanguages(storeUuid)
                    .subscribe((langs: LanguageData[]) => {
                        this.allLanguage = langs;
                        this.selectedLanguage = langs.find(({isocode}) => isocode === currentLang);
                        this.languages = langs.filter(({isocode}) => isocode !== currentLang);
                    });

            });
    }

    useLanguage(langIso: string) {
        if (this.translate.currentLang !== langIso) {
            this.selectedLanguage = this.allLanguage.find(({isocode}) => isocode === langIso);
            this.languages = this.allLanguage.filter(({isocode}) => isocode !== langIso);
            this.translate.use(langIso)
            if (this.authService.isAuthenticated()) {
                this.userService.setDefaultLanguage(langIso)
            }
        }
    }
}
