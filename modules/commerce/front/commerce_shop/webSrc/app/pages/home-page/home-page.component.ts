import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {StoreData} from "../../core/data/store.data";
import {fadeInAnimation} from "../../core/animation/fadeInAnimation.animation";
declare var $: any;

@Component({
  animations : [fadeInAnimation],
  host: { '[@fadeInAnimation]': '' },
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent implements OnInit, AfterViewInit{



  title = 'GoodMood - Home page';

  @ViewChild('rev_slider', {static: false}) slider: ElementRef;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    $(this.slider.nativeElement).show().revolution({
      sliderType:"standard",
      sliderLayout:"fullwidth",
      dottedOverlay:"none",
      delay:9000,
      navigation: {
        keyboardNavigation:"off",
        keyboard_direction: "horizontal",
        mouseScrollNavigation:"off",
        mouseScrollReverse:"default",
        onHoverStop:"off",
        arrows: {
          style:"metis",
          enable:true,
          hide_onmobile:false,
          hide_onleave:true,
          hide_delay:200,
          hide_delay_mobile:1200,
          tmp:'',
          left: {
            h_align:"left",
            v_align:"center",
            h_offset:20,
            v_offset:0
          },
          right: {
            h_align:"right",
            v_align:"center",
            h_offset:20,
            v_offset:0
          }
        }
        ,
        bullets: {
          enable:true,
          hide_onmobile:false,
          style:"custom",
          hide_onleave:false,
          direction:"horizontal",
          h_align:"center",
          v_align:"bottom",
          h_offset:0,
          v_offset:20,
          space:5,
          tmp:''
        }
      },
      responsiveLevels:[1240,1024,778,480],
      visibilityLevels:[1240,1024,778,480],
      gridwidth:[1920,1024,778,480],
      gridheight:[780,768,960,720],
      lazyType:"none",
      shadow:0,
      spinner:"spinner0",
      stopLoop:"off",
      stopAfterLoops:-1,
      stopAtSlide:-1,
      shuffle:"off",
      autoHeight:"off",
      disableProgressBar:"on",
      hideThumbsOnMobile:"off",
      hideSliderAtLimit:0,
      hideCaptionAtLimit:0,
      hideAllCaptionAtLilmit:0,
      debugMode:false,
      fallbacks: {
        simplifyAll:"off",
        nextSlideOnWindowFocus:"off",
        disableFocusListener:false,
      }
    });

    var $themeSlickSlider = $('.theme-slick-slider');


    $themeSlickSlider.each(function(){

      /*Setting Variables*/
      let $this = $(this),
          $setting = $this.data('slick-setting'),
          $autoPlay = $setting.autoplay ? $setting.autoplay : false,
          $autoPlaySpeed = parseInt($setting.autoplaySpeed, 10) || 2000,
          $speed = parseInt($setting.speed, 10) || 2000,
          $asNavFor = $setting.asNavFor ? $setting.asNavFor : null,
          $appendArrows = $setting.appendArrows ? $setting.appendArrows : $this,
          $appendDots = $setting.appendDots ? $setting.appendDots : $this,
          $arrows = $setting.arrows ? $setting.arrows : false,
          $prevArrow = $setting.prevArrow ? '<button class="'+ $setting.prevArrow.buttonClass +'"><i class="'+ $setting.prevArrow.iconClass +'"></i></button>' : '<button class="slick-prev">previous</button>',
          $nextArrow = $setting.nextArrow ? '<button class="'+ $setting.nextArrow.buttonClass +'"><i class="'+ $setting.nextArrow.iconClass +'"></i></button>' : '<button class="slick-next">next</button>',
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
          $verticalSwiping = $setting.verticalSwiping ? $setting.verticalSwiping : false

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
        prevArrow: $prevArrow,
        nextArrow: $nextArrow,
        responsive: $responsiveArray
      });

    });

    /*=====  End of slick slider active  ======*/


  }

}
