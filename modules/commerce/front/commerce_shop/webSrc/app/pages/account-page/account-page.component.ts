import {Component, OnInit} from '@angular/core';
import {fadeInAnimation} from '../../core/animation/fadeInAnimation.animation';
import {UserService} from '../../core/service/user.service';
import {UserData} from '../../core/data/user.data';

@Component({
    animations: [fadeInAnimation],
    host: {'[@fadeInAnimation]': ''},
    selector: 'app-account-page',
    templateUrl: './account-page.component.html',
    styleUrls: ['./account-page.component.scss']
})
export class AccountPageComponent implements OnInit {

    currentUser: UserData;

    constructor(private userService: UserService) {
    }

    ngOnInit() {
        this.userService.currentUser.subscribe(user => {
            this.currentUser = user;
        })
    }

}
