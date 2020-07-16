import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SearchResult} from "../../../core/data/search.result.data";
import {fadeInAnimation} from "../../../core/animation/fadeInAnimation.animation";

declare var $: any;

@Component({
    animations : [fadeInAnimation],
    host: { '[@fadeInAnimation]': '' },
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

    @Input()
    searchResult: SearchResult;

    @Input()
    facets: Map<string, string[]> = new Map<string, string[]>();

    @Output()
    addFacetEvent: EventEmitter<any> = new EventEmitter<any>();
    @Output()
    removeFacetEvent : EventEmitter<any> = new EventEmitter<any>();

    constructor() {
    }

    ngOnInit() {

    /*=============================================
    =            price filter active            =
    =============================================*/

        $('#price-range').slider({
            range: true,
            min: 25,
            max: 350,
            values: [25, 350],
            slide: function (event, ui) {
                $('#price-amount').val('Price: ' + '$' + ui.values[0] + ' - $' + ui.values[1]);
            }
        });
        $('#price-amount').val('Price: ' + '$' + $('#price-range').slider('values', 0) +
            ' - $' + $('#price-range').slider('values', 1));


        /*=====  End of price filter active  ======*/


        /*=============================================
        =            sidebar dropdown            =
        =============================================*/

        $('#single-sidebar-widget__dropdown li.has-children').append('<i class="fa fa-angle-down"></i>');

        $('#single-sidebar-widget__dropdown li.has-children > i').on('click', function () {
            $(this).parent().toggleClass('active');

            $(this).siblings('.sub-menu').slideToggle();
        });

        /*=====  End of sidebar dropdown  ======*/
    }

}
