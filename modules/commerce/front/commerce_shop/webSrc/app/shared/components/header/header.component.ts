import {AfterViewInit, Component, ElementRef, HostListener, Input, OnInit, ViewChild} from '@angular/core';
import {StoreData} from "../../../core/data/store.data";
import {AuthService} from "../../../core/service/auth.service";

declare var $: any;

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, AfterViewInit {
    @Input() store: StoreData;

    @ViewChild('headerSticky', {static: false}) headerSticky: ElementRef;

    authorized = false;

    constructor(private authService: AuthService) {
    }

    ngOnInit() {
    }

    isLoggedIn(){
        return this.authService.isAuthenticated();
    }

    @HostListener('window:scroll', ['$event'])
    scrollHandler(event) {
        let $window = $(window),
            $headerSticky = $(this.headerSticky.nativeElement);

        let scroll = $window.scrollTop();

        if (scroll >= 200 && $window.width() > 767) {
            $headerSticky.addClass('is-sticky');
        } else {
            $headerSticky.removeClass('is-sticky');
        }
    }

    ngAfterViewInit(): void {

        /*=============================================
        =            mobile menu activation            =
        =============================================*/

        $('#mobile-menu-trigger').on('click', function () {
            $('#offcanvas-mobile-menu').removeClass('inactive').addClass('active');
        });


        $('#offcanvas-menu-close-trigger').on('click', function () {
            $('#offcanvas-mobile-menu').removeClass('active').addClass('inactive');
        });

        /*=====  End of mobile menu activation  ======*/


    }


}
