import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../core/service/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../core/service/user.service";
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";

@Component({
    animations: [fadeInAnimation],
    host: { '[@fadeInAnimation]': '' },
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    registerForm: FormGroup;
    submitted = false;
    loading = false;
    error = '';

    constructor(private formBuilder: FormBuilder,
                private userService: UserService,
                private route: ActivatedRoute,
                private authService: AuthService,
                private router: Router) {
    }

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
            email: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', Validators.required],
            firstName: ['', Validators.required],
            lastName: ['', Validators.required]
        });
    }

    get f() {
        return this.registerForm.controls;
    }

    onSubmit() {
        this.error = '';
        this.submitted = true;

        // stop here if form is invalid
        if (this.registerForm.invalid) {
            return;
        }
        this.loading = true;
        this.userService
            .register(this.registerForm.value)
            .subscribe(data => {
                    this.userService.updateCurrentUser()
                    this.authService.getRedirectUrl()
                        .subscribe(val => {
                            this.router.navigate(val);
                        });
                },
                error => {
                    this.error = `form.login.errors.${error.status}`;
                    this.loading = false;
                });
        this.loading = false;
    }
}
