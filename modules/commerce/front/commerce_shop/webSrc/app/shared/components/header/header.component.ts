import {
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    HostListener,
    Input,
    OnInit,
    Output,
    ViewChild
} from '@angular/core';
import {StoreData} from '../../../core/data/store.data';
import {AuthService} from '../../../core/service/auth.service';
import {UserData} from '../../../core/data/user.data';
import {UserService} from '../../../core/service/user.service';

declare var $: any;

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, AfterViewInit {
    @Input() store: StoreData;

    @ViewChild('headerSticky', {static: false}) headerSticky: ElementRef;

    @Output()
    toggleMobileMenuEvent = new EventEmitter<any>();
    currentUser: UserData;

    constructor(private authService: AuthService, private userService: UserService) {
    }

    ngOnInit() {
        this.userService
            .currentUser
            .subscribe(u => {
                this.currentUser = u;
            })
        if (this.authService.isAuthenticated() && !this.currentUser) {
            this.userService.updateCurrentUser();
        }
    }

    isLoggedIn() {
        return this.authService.isAuthenticated() && this.currentUser;
    }

    logout() {
        this.authService.logout();
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

    toggleMobileMenu() {
        this.toggleMobileMenuEvent.emit()
    }

    ngAfterViewInit(): void {


        /*=============================================
        =     active and deactive search overlay      =
        =============================================*/

        $('#search-icon, #search-icon-2').on('click', function () {
            $('#search-overlay').addClass('active-search-overlay');
            $('body').addClass('active-body-search-overlay');
        });

        $('#search-close-icon').on('click', function () {
            $('#search-overlay').removeClass('active-search-overlay');
            $('body').removeClass('active-body-search-overlay');
        });


        /*=====  End of active and deactive search overlay  ======*/

    }


}
