import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild, ViewChildren} from '@angular/core';

declare var $: any;

@Component({
    selector: 'app-slick-slider',
    templateUrl: './slick-slider.component.html',
    styleUrls: ['./slick-slider.component.scss']
})
export class SlickSliderComponent implements AfterViewInit {

    @ViewChild('slickSlider', {static: false})
    slickSlider: ElementRef;

    @Input()
    styleClasses: string;
    @Input()
    slidesToShow: number = 1;
    @Input()
    slidesToScroll = 1;
    @Input()
    arrows = false;
    @Input()
    autoplay = false;
    @Input()
    centerMode = false;
    @Input()
    focusOnSelect = false;
    @Input()
    infinite = true;
    @Input()
    pauseOnHover = true;
    @Input()
    swipe = true;
    @Input()
    vertical = false;
    @Input()
    swipeToSlide = false;
    @Input()
    variableWidth = false;
    @Input()
    centerPadding = '50px';
    @Input()
    rtl = false;
    @Input()
    autoplaySpeed = 2000;
    @Input()
    dots = false;
    @Input()
    fade = false;
    @Input()
    speed = 2000;
    @Input()
    asNavFor: string = null;
    @Input()
    appendArrows;
    @Input()
    rows = 1;
    @Input()
    appendDots;
    @Input()
    verticalSwiping = false;
    @Input()
    slick_responsive: Object[];

    @Input()
    prevArrow = {buttonClass: "slick-prev", iconClass: "fa fa-angle-left"};
    @Input()
    nextArrow = {buttonClass: "slick-next", iconClass: "fa fa-angle-right"};

    constructor() {
    }

    ngAfterViewInit() {
        this.init()
    }

    private init(): void {
      /*=============================================
      =            slick slider active            =
      =============================================*/
        let $html = $('html');
        let $body = $('body');
        let $themeSlickSlider = $(this.slickSlider.nativeElement);

        /*For RTL*/
        if ($html.attr("dir") == "rtl" || $body.attr("dir") == "rtl") {
            $themeSlickSlider.attr("dir", "rtl");
        }

        /*Setting Variables*/
        let $this = $themeSlickSlider,
            $autoPlay = this.autoplay,
            $autoPlaySpeed = this.autoplaySpeed,
            $speed = this.speed,
            $asNavFor = this.asNavFor,
            $appendArrows = this.appendArrows ? this.appendArrows : $this,
            $appendDots = this.appendDots ? this.appendDots : $this,
            $arrows = this.arrows,
            $prevArrow = this.prevArrow ? '<button class="' + this.prevArrow.buttonClass + '"><i class="' + this.prevArrow.iconClass + '"></i></button>' : '<button class="slick-prev">previous</button>',
            $nextArrow = this.nextArrow ? '<button class="' + this.nextArrow.buttonClass + '"><i class="' + this.nextArrow.iconClass + '"></i></button>' : '<button class="slick-next">next</button>',
            $centerMode = this.centerMode,
            $centerPadding = this.centerPadding,
            $dots = this.dots,
            $fade = this.fade,
            $focusOnSelect = this.focusOnSelect,
            $infinite = this.infinite,
            $pauseOnHover = this.pauseOnHover,
            $rows = this.rows,
            $slidesToShow = this.slidesToShow,
            $slidesToScroll = this.slidesToScroll,
            $swipe = this.swipe,
            $swipeToSlide = this.swipeToSlide,
            $variableWidth = this.variableWidth,
            $vertical = this.vertical,
            $verticalSwiping = this.verticalSwiping,
            $rtl = !!(this.rtl || $html.attr('dir="rtl"') || $body.attr('dir="rtl"'));

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
            responsive: this.slick_responsive
        });

        /*=====  End of slick slider active  ======*/
    }

}
