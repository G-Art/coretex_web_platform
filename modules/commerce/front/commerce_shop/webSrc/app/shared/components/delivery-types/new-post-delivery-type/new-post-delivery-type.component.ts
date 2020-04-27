import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AbstractDeliveryType} from '../abstract-delivery-type';
import {FormBuilder, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {CartService} from '../../../../core/service/cart.service';
import {Subscription} from 'rxjs';
import {AddressData} from '../../../../core/data/address.data';
import {UserData} from '../../../../core/data/user.data';

@Component({
    selector: 'app-new-post-delivery-type',
    templateUrl: './new-post-delivery-type.component.html',
    styleUrls: ['./new-post-delivery-type.component.scss']
})
export class NewPostDeliveryTypeComponent extends AbstractDeliveryType implements OnInit, OnDestroy {

    @ViewChild(FormGroupDirective) deliveryTypeInfoForm: FormGroupDirective;

    formChanged: boolean = true;
    deliveryTypeAddressInfoForm: FormGroup;
    deliveryTypeWarehouseInfoForm: FormGroup;

    private onSubmitSubscription: Subscription;

    constructor(private cartService: CartService,
                private formBuilder: FormBuilder) {
        super();
    }

    ngOnInit(): void {
        let address: AddressData = this.cart.address;
        let customer: UserData = this.cart.user;

        this.deliveryTypeAddressInfoForm = this.formBuilder.group({
            deliveryType: [this.deliveryType.uuid],
            firstName: [address ? address.firstName : '', Validators.required],
            lastName: [address ? address.lastName : '', Validators.required],
            email: [customer ? customer.email : '', Validators.required],
            phone: [address ? address.phone : '', Validators.required],
            addressLine1: [address ? address.addressLine1 : '', Validators.required],
            addressLine2: [address ? address.addressLine2 : ''],
            city: ['', Validators.required],
            zipCode: [address ? address.postalCode : '', Validators.required],
            createAccount: [false]

        });
        this.deliveryTypeWarehouseInfoForm = this.formBuilder.group({
            deliveryType: [this.deliveryType.uuid],
            firstName: [address ? address.firstName : '', Validators.required],
            lastName: [address ? address.lastName : '', Validators.required],
            email: [customer ? customer.email : '', Validators.required],
            phone: [address ? address.phone : '', Validators.required],
            city: ['', Validators.required],
            branch: ['', Validators.required],
            createAccount: [false]
        });

        this.onSubmitSubscription = this.cartService.submitOrder.subscribe(e => {
            this.submit(this.deliveryTypeInfoForm.form)
        });
    }

    ngOnDestroy(): void {
        this.onSubmitSubscription.unsubscribe();
    }

    submit(deliveryTypeInfoForm: FormGroup): void {
        this.cartService.addDeliveryInfo(deliveryTypeInfoForm.value);
    }
}
