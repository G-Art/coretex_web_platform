import {Component, Input, OnInit} from '@angular/core';
import {CategoryService} from "../../../core/service/category.service";
import {CategoryData} from "../../../core/data/category.data";
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";
import {Observable} from "rxjs";

declare var $: any;

@Component({
    animations : [fadeInAnimation],
    host: { '[@fadeInAnimation]': '' },
    selector: 'app-navigation-menu',
    templateUrl: './navigation-menu.component.html',
    styleUrls: ['./navigation-menu.component.scss']
})
export class NavigationMenuComponent implements OnInit {

    categories:Observable<CategoryData[]>;

    @Input()
    mobileMenuOpen:boolean = false;

    @Input()
    renderMobile:boolean = false;

    constructor(private categoryService: CategoryService) {
    }

    toggleMobileMenu(){
        this.mobileMenuOpen = !this.mobileMenuOpen;
    }

    ngOnInit() {
        this.categories = this.categoryService.getCategoriesMenu();
    }

}
