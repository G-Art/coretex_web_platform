import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../core/service/auth.service";
import {CookieService} from "ngx-cookie-service";
import {UserService} from '../../../core/service/user.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    loginForm: FormGroup;
    submitted = false;
    loading = false;
    error = '';

    constructor(private formBuilder: FormBuilder,
                private userService: UserService,
                private authService: AuthService,
                private cookieService: CookieService,
                private route: ActivatedRoute,
                private router: Router,) {
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            email: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    get f() {
        return this.loginForm.controls;
    }

    onSubmit() {
        this.error = '';
        this.submitted = true;

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        this.loading = true;
        this.authService
            .login({login: this.f.email.value, password: this.f.password.value})
            .subscribe(
                data => {
                    // this.userService.updateCurrentUser()
                    this.authService.getRedirectUrl()
                        .subscribe(val => {
                            this.router.navigate(val);
                        });
                },
                error => {
                    this.error = `form.login.errors.${error.status}`;
                    this.loading = false;
                });
        this.loading = false
    }
}
