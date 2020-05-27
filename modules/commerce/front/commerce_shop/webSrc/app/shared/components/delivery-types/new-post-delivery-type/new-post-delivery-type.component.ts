import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AbstractDeliveryType} from '../abstract-delivery-type';
import {FormBuilder, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {CartService} from '../../../../core/service/cart.service';
import {noop, Observable, Observer, of, Subscription} from 'rxjs';
import {AddressData} from '../../../../core/data/address.data';
import {map, switchMap, tap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NewPostSettlements} from '../../../../core/data/newpost/new-post-settlements.data';
import {TypeaheadMatch} from 'ngx-bootstrap/typeahead/typeahead-match.class';
import {NewPostWarehouse} from '../../../../core/data/newpost/new-post-warehouse.data';

@Component({
    selector: 'app-new-post-delivery-type',
    templateUrl: './new-post-delivery-type.component.html',
    styleUrls: ['./new-post-delivery-type.component.scss']
})
export class NewPostDeliveryTypeComponent extends AbstractDeliveryType implements OnInit, OnDestroy {

    @ViewChild(FormGroupDirective) deliveryTypeInfoForm: FormGroupDirective;

    error = '';
    deliveryTypeAddressInfoForm: FormGroup;
    deliveryTypeWarehouseInfoForm: FormGroup;
    citySuggestions$: Observable<NewPostSettlements[]>;
    brunchSuggestions$: Observable<NewPostWarehouse[]>;

    private onSubmitSubscription: Subscription;

    constructor(private cartService: CartService,
                private formBuilder: FormBuilder,
                private http: HttpClient) {
        super();
    }


    ngOnInit(): void {
        let address: AddressData = this.cart.address;

        this.citySuggestions$ = new Observable((observer: Observer<string>) => {
            observer.next(this.deliveryTypeInfoForm.form.controls['city'].value || '');
        }).pipe(
            switchMap((query: string) => {
                return this.http.get<NewPostSettlements[]>(
                    `/newpost/${this.deliveryType.deliveryService}/settlements`, {
                        params: {q: query}
                    }).pipe(
                    map(data => data || []),
                    tap(() => noop, err => {
                        // ignore
                    })
                );
            })
        );

        this.brunchSuggestions$ = new Observable((observer: Observer<string>) => {
            observer.next(this.deliveryTypeInfoForm.form.controls.cityRef.value);
        }).pipe(
            switchMap((ref: string) => {
                return this.http.get<NewPostWarehouse[]>(
                    `/newpost/${this.deliveryType.deliveryService}/warehouses`, {
                        params: {ref: ref}
                    }).pipe(
                    map(data => data || []),
                    tap(() => noop, err => {
                        // ignore
                    })
                );
            })
        );
        let cityRef = address ? address.additionalInfo['cityRef'] : null;
        this.deliveryTypeAddressInfoForm = this.formBuilder.group({
            deliveryType: [this.deliveryType.uuid],
            firstName: [address ? address.firstName : '', Validators.required],
            lastName: [address ? address.lastName : '', Validators.required],
            email: [address ? address.email : '', Validators.required],
            phone: [address ? address.phone : '', Validators.required],
            addressLine1: [address ? address.addressLine1 : '', Validators.required],
            addressLine2: [address ? address.addressLine2 : ''],
            city: [address ? address.city : '', Validators.required],
            cityRef: [cityRef],
            zipCode: [address ? address.postalCode : '', Validators.required],
            createAccount: [false]
        });
        let branch = address ? address.additionalInfo['branch'] : null;
        this.deliveryTypeWarehouseInfoForm = this.formBuilder.group({
            deliveryType: [this.deliveryType.uuid],
            firstName: [address ? address.firstName : '', Validators.required],
            lastName: [address ? address.lastName : '', Validators.required],
            email: [address ? address.email : '', Validators.required],
            phone: [address ? address.phone : '', Validators.required],
            city: [address ? address.city : '', Validators.required],
            cityRef: [cityRef],
            branch: [{value: branch, disabled: branch === null}, Validators.required],
            branchRef: [address ? address.additionalInfo['branchRef'] : null],
            createAccount: [false]
        });

        this.onSubmitSubscription = this.cartService.beforeOrderPlace.subscribe(e => {
            this.submit(this.deliveryTypeInfoForm.form)
        });
    }

    ngOnDestroy(): void {
        this.onSubmitSubscription.unsubscribe();
    }

    submit(deliveryTypeInfoForm: FormGroup): void {
        this.error = '';
        if (deliveryTypeInfoForm.valid) {
            let errorCode: number;
            this.cartService.addDeliveryInfo(deliveryTypeInfoForm.value, code => {
                errorCode = code
            });
            if (errorCode && errorCode !== 200) {
                this.error = `form.common.errors.${errorCode}`;
            }
        }
    }

    selectCity($data: TypeaheadMatch) {
        if(this.deliveryTypeInfoForm.form.controls.cityRef.value !== $data.item.Ref){
            this.deliveryTypeInfoForm.form.patchValue({cityRef: $data.item.Ref});
            this.deliveryTypeInfoForm.form.patchValue({branchRef: null});
            this.deliveryTypeInfoForm.form.patchValue({branch: null});
            this.deliveryTypeInfoForm.form.controls.branch?.enable();
        }
    }

    selectBranch($event: TypeaheadMatch) {
        this.deliveryTypeInfoForm.form.patchValue({branchRef: $event.item.Ref});
    }

}
