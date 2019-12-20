import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';

declare var $: any;

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, AfterViewInit {

    @ViewChild('headerSticky', {static: false}) headerSticky: ElementRef;

    constructor() {
    }

    ngOnInit() {
    }

    ngAfterViewInit(): void {

        let $window = $(window),
            $headerSticky = $(this.headerSticky.nativeElement);

        /*=====  End of variables  ======*/


        /*=============================================
        =            sticky header            =
        =============================================*/

        $window.on('scroll', function () {
            if ($window.scrollTop() >= 200 && $window.width() > 767) {
                $headerSticky.addClass('is-sticky');
            } else {
                $headerSticky.removeClass('is-sticky');
            }

            //code for scroll top
            var scroll = $window.scrollTop();

            if (scroll >= 400) {
                $('.scroll-top').fadeIn();
            } else {
                $('.scroll-top').fadeOut();
            }

        });

        /*=====  End of sticky header  ======*/


        /*=============================================
        =            scroll top            =
        =============================================*/

        $('.scroll-top').on('click', function () {
            $('html,body').animate({
                scrollTop: 0
            }, 2000);
        });

        /*=====  End of scroll top  ======*/


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
