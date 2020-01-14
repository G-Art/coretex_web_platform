import {Component, Input, OnInit} from '@angular/core';
import {CategoryService} from "../../../core/service/category.service";
import {CategoryData} from "../../../core/data/category.data";

declare var $: any;

@Component({
    selector: 'app-navigation-menu',
    templateUrl: './navigation-menu.component.html',
    styleUrls: ['./navigation-menu.component.scss']
})
export class NavigationMenuComponent implements OnInit {

    categories:CategoryData[];

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

      this.categoryService.getCategoriesMenu().subscribe(next => {
        this.categories = next;
      });
    }

}
