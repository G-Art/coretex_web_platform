import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'app-currency-selector',
    templateUrl: './currency-selector.component.html',
    styleUrls: ['./currency-selector.component.css']
})
export class CurrencySelectorComponent implements OnInit {

    selectedCurrency: string;

    currencies: string[];

    constructor(private router: Router) {
    }

    ngOnInit() {
        this.currencies = ['USD', 'EUR', 'GBP'];
        this.selectedCurrency = 'USD';
    }



}
