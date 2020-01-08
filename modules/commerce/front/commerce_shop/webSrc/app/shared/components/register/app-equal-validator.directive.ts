import {Directive, Input} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from "@angular/forms";

@Directive({
    selector: '[appEqualValidator]',
    providers: [{
        provide: NG_VALIDATORS,
        useExisting: AppEqualValidatorDirective,
        multi: true
    }]
})
export class AppEqualValidatorDirective implements Validator {
    @Input() appEqualValidator: string;

    constructor() {
    }

    validate(control: AbstractControl): ValidationErrors | null {
        const controlToCompare = control.parent.get(this.appEqualValidator);
        if (controlToCompare && controlToCompare.value !== control.value) {
            return {'notEqual': true}
        }
        return null;
    }

}
