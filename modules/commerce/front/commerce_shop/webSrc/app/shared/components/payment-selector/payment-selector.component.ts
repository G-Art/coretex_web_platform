import {Component, Input, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {CartData} from '../../../core/data/cart.data';
import {DeliveryTypeService} from '../../../core/service/delivery-type.service';
import {PaymentType} from '../../../core/data/payment-type.data';
import {TranslateService} from '@ngx-translate/core';
import {CartService} from '../../../core/service/cart.service';

@Component({
    selector: 'app-payment-selector',
    templateUrl: './payment-selector.component.html',
    styleUrls: ['./payment-selector.component.scss']
})
export class PaymentSelectorComponent implements OnInit {


    @ViewChild('paymentsContainer', {static: true, read: ViewContainerRef}) paymentsContainer: ViewContainerRef;

    private _cart: CartData;

    payments: PaymentType[] = null;

    constructor(public translate: TranslateService,
        private deliveryTypeService: DeliveryTypeService,
        private cartService: CartService) {
    }

    ngOnInit(): void {
    }

    showPayments(): boolean {
        return !!this._cart && !!this._cart.deliveryType;
    }

    get cart(): CartData {
        return this._cart;
    }

    @Input()
    set cart(value: CartData) {
        this._cart = value;
        this.updatePaymentMethods()
    }

    private updatePaymentMethods(): void {
        this.payments = null;
        if (this.showPayments()) {
            let deliveryType = this._cart.deliveryType;

            this.deliveryTypeService.getPaymentTypesForDeliveryType(deliveryType)
                .subscribe(payments => {
                    this.payments = payments
                })
        }
    }

    selectPayment(e): void{
        let code = e.target.value;
        let payment = this.payments.find(p => p.code === code)
        this.cartService.addPaymentType(payment)
    }
}
