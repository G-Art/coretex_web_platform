import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductService} from "../../core/service/product.service";
import {ProductDetailData} from "../../core/data/product-detail.data";

declare var $: any;

@Component({
    selector: 'app-product-detail-page',
    templateUrl: './product-detail-page.component.html',
    styleUrls: ['./product-detail-page.component.scss']
})
export class ProductDetailPageComponent implements OnInit {

    productDetail: ProductDetailData;

    constructor(private activatedRoute: ActivatedRoute,
                private productService: ProductService) {
    }

    ngOnInit() {
        this.init();

        this.activatedRoute.params.subscribe(routeParams => {
            this.productService.getProductDetail(routeParams.code)
                .subscribe(data => this.productDetail = data);
        })

    }

    private init() {
        /*=============================================
=            slick slider active            =
=============================================*/
        let $html = $('html');
        let $body = $('body');
        let $themeSlickSlider = $('.theme-slick-slider');

        /*For RTL*/
        if ($html.attr("dir") == "rtl" || $body.attr("dir") == "rtl") {
            $themeSlickSlider.attr("dir", "rtl");
        }

        $themeSlickSlider.each(function () {

            /*Setting Variables*/
            var $this = $(this),
                $setting = $this.data('slick-setting'),
                $autoPlay = $setting.autoplay ? $setting.autoplay : false,
                $autoPlaySpeed = parseInt($setting.autoplaySpeed, 10) || 2000,
                $speed = parseInt($setting.speed, 10) || 2000,
                $asNavFor = $setting.asNavFor ? $setting.asNavFor : null,
                $appendArrows = $setting.appendArrows ? $setting.appendArrows : $this,
                $appendDots = $setting.appendDots ? $setting.appendDots : $this,
                $arrows = $setting.arrows ? $setting.arrows : false,
                $prevArrow = $setting.prevArrow ? '<button class="' + $setting.prevArrow.buttonClass + '"><i class="' + $setting.prevArrow.iconClass + '"></i></button>' : '<button class="slick-prev">previous</button>',
                $nextArrow = $setting.nextArrow ? '<button class="' + $setting.nextArrow.buttonClass + '"><i class="' + $setting.nextArrow.iconClass + '"></i></button>' : '<button class="slick-next">next</button>',
                $centerMode = $setting.centerMode ? $setting.centerMode : false,
                $centerPadding = $setting.centerPadding ? $setting.centerPadding : '50px',
                $dots = $setting.dots ? $setting.dots : false,
                $fade = $setting.fade ? $setting.fade : false,
                $focusOnSelect = $setting.focusOnSelect ? $setting.focusOnSelect : false,
                $infinite = $setting.infinite ? $setting.infinite : true,
                $pauseOnHover = $setting.pauseOnHover ? $setting.pauseOnHover : true,
                $rows = parseInt($setting.rows, 10) || 1,
                $slidesToShow = parseInt($setting.slidesToShow, 10) || 1,
                $slidesToScroll = parseInt($setting.slidesToScroll, 10) || 1,
                $swipe = $setting.swipe ? $setting.swipe : true,
                $swipeToSlide = $setting.swipeToSlide ? $setting.swipeToSlide : false,
                $variableWidth = $setting.variableWidth ? $setting.variableWidth : false,
                $vertical = $setting.vertical ? $setting.vertical : false,
                $verticalSwiping = $setting.verticalSwiping ? $setting.verticalSwiping : false,
                $rtl = $setting.rtl || $html.attr('dir="rtl"') || $body.attr('dir="rtl"') ? true : false;

            /*Responsive Variable, Array & Loops*/
            var $responsiveSetting = typeof $this.data('slick-responsive') !== 'undefined' ? $this.data('slick-responsive') : '',
                $responsiveSettingLength = $responsiveSetting.length,
                $responsiveArray = [];
            for (var i = 0; i < $responsiveSettingLength; i++) {
                $responsiveArray[i] = $responsiveSetting[i];

            }

            /*Slider Start*/
            $this.slick({
                autoplay: $autoPlay,
                autoplaySpeed: $autoPlaySpeed,
                speed: $speed,
                asNavFor: $asNavFor,
                appendArrows: $appendArrows,
                appendDots: $appendDots,
                arrows: $arrows,
                dots: $dots,
                centerMode: $centerMode,
                centerPadding: $centerPadding,
                fade: $fade,
                focusOnSelect: $focusOnSelect,
                infinite: $infinite,
                pauseOnHover: $pauseOnHover,
                rows: $rows,
                slidesToShow: $slidesToShow,
                slidesToScroll: $slidesToScroll,
                swipe: $swipe,
                swipeToSlide: $swipeToSlide,
                variableWidth: $variableWidth,
                vertical: $vertical,
                verticalSwiping: $verticalSwiping,
                rtl: $rtl,
                prevArrow: $prevArrow,
                nextArrow: $nextArrow,
                responsive: $responsiveArray
            });

        });


        /*=====  End of slick slider active  ======*/

        /*=============================================
        =            quantity counter            =
        =============================================*/

        $('.pro-qty').append('<a href="#" class="inc qty-btn"><i class="pe-7s-plus"></i></a>');
        $('.pro-qty').prepend('<a href="#" class= "dec qty-btn"><i class="pe-7s-less"></i></a>');
        $('.qty-btn').on('click', function (e) {
            e.preventDefault();
            var $button = $(this);
            var oldValue = $button.parent().find('input').val();
            if ($button.hasClass('inc')) {
                var newVal = parseFloat(oldValue) + 1;
            } else {
                // Don't allow decrementing below zero
                if (oldValue > 0) {
                    var newVal = parseFloat(oldValue) - 1;
                } else {
                    newVal = 0;
                }
            }
            $button.parent().find('input').val(newVal);
        });

        /*=====  End of quantity counter  ======*/

        /*=============================================
        =            zoom and light gallery active            =
        =============================================*/

        $('.product-details-big-image-slider-wrapper .single-image').zoom();

        //lightgallery
        var productThumb = $(".product-details-big-image-slider-wrapper .single-image img"),
            imageSrcLength = productThumb.length,
            images = [];
        for (var i = 0; i < imageSrcLength; i++) {
            images[i] = {"src": productThumb[i].src};
        }

        $('.btn-zoom-popup').on('click', function () {
            $(this).lightGallery({
                thumbnail: false,
                dynamic: true,
                autoplayControls: false,
                download: false,
                actualSize: false,
                share: false,
                hash: false,
                index: 0,
                dynamicEl: images
            });
        });

        /*=====  End of zoom active  ======*/


        /*=============================================
        =            sidevar sticky active            =
        =============================================*/

        $('.sidebar-sticky').stickySidebar({
            topSpacing: 90,
            bottomSpacing: -90,
            minWidth: 768
        });

        /*=====  End of sidevar sticky active  ======*/
    }

}
