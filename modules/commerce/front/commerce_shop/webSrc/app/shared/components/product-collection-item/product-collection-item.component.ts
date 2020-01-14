import {Component, HostBinding, OnInit} from '@angular/core';
declare var $: any;

@Component({
  selector: 'app-product-collection-item',
  templateUrl: './product-collection-item.component.html',
  styleUrls: ['./product-collection-item.component.scss']
})
export class ProductCollectionItemComponent implements OnInit {

  @HostBinding('class')
  columnsClass:string = 'col-lg-4';

  itemView:string = 'grid';

  constructor() { }

  ngOnInit() {

    /*=============================================
    =            custom quick view            =
    =============================================*/


    //final width --> this is the quick view image slider width
    //maxQuickWidth --> this is the max-width of the quick-view panel
    var sliderFinalWidth = 400,
        maxQuickWidth = 900;

    //open the quick view panel
    $('.cd-trigger').on('click', function (event) {
      event.preventDefault();
      let selectedImage = $(this)
              .closest('.single-grid-product, .single-list-product')
              .find('.single-grid-product__image .image-wrap, .single-list-product__image .image-wrap')
              .children('img').eq(0),
          id = $(this).attr('href'),
          slectedImageUrl = selectedImage.attr('src');

      $('body').addClass('overlay-layer');
      animateQuickView(id, selectedImage, sliderFinalWidth, maxQuickWidth, 'open');

      //update the visible slider image in the quick view panel
      //you don't need to implement/use the updateQuickView if retrieving the quick view data with ajax
      //updateQuickView(slectedImageUrl);
    });

    //close the quick view panel
    $('body').on('click', function (event) {
      if ($(event.target).is('.cd-close') || $(event.target).is('body.overlay-layer')) {
        event.preventDefault();
        var id = $(this).find('.cd-quick-view.add-content');
        closeQuickView(id, sliderFinalWidth, maxQuickWidth);
      }
    });

    $(document).keyup(function (event) {
      //check if user has pressed 'Esc'
      if (event.which == '27') {
        var id = $('body').find('.cd-quick-view.add-content');
        closeQuickView(id, sliderFinalWidth, maxQuickWidth);
      }
    });

    //quick view slider Update On Navigation
    $('.cd-quick-view').on('click', '.cd-slider-navigation a', function (event) {
      event.preventDefault();
      var $this = $(this);
      updateSliderNav($this);
    });

    //quick view slider Update On Pagination
    $('.cd-quick-view').on('click', '.cd-slider-pagination a', function (event) {
      event.preventDefault();
      var $this = $(this),
          $li = $this.parents('li');

      $li.addClass('active').siblings().removeClass('active');

      updateSliderPage($this);
    });

    //center quick-view on window resize
    $(window).on('resize', function () {
      if ($('.cd-quick-view').hasClass('is-visible')) {
        window.requestAnimationFrame(resizeQuickView);
      }
    });

    /*Update Quick View Slider With Navigation*/
    function updateSliderNav(navigation) {

      var sliderConatiner = navigation.parents('.cd-slider-wrapper').find('.cd-slider'),
          activeSlider = sliderConatiner.children('.selected'),
          sliderPanilation = navigation.parents('.cd-slider-wrapper').children('.cd-slider-pagination');

      sliderPanilation.children('li').removeClass('active');

      activeSlider.removeClass('selected');
      if (navigation.hasClass('cd-next')) {
        if (!activeSlider.is(':last-child')) {
          activeSlider.next().addClass('selected');
          sliderPanilation.children('li').eq(activeSlider.next().index()).addClass('active');
        } else {
          sliderConatiner.children('li').eq(0).addClass('selected');
          sliderPanilation.children('li').eq(sliderConatiner.children('li').eq(0).index()).addClass('active');
        }
      } else {
        if (!activeSlider.is(':first-child')) {
          activeSlider.prev().addClass('selected');
          sliderPanilation.children('li').eq(activeSlider.prev().index()).addClass('active');
        } else {
          sliderConatiner.children('li').last().addClass('selected');
          sliderPanilation.children('li').eq(sliderConatiner.children('li').last().index()).addClass('active');
        }
      }
    }

    /*Update Quick View Slider With Pagination*/
    function updateSliderPage(pagination) {

      var sliderConatiner = pagination.parents('.cd-slider-wrapper').find('.cd-slider'),
          sliderItem = sliderConatiner.children('li'),
          paginationIndex = pagination.parent('li').index();

      sliderItem.removeClass('selected');
      sliderItem.eq(paginationIndex).addClass('selected');
    }

    /*Resize Quick View*/
    function resizeQuickView() {
      var quickViewLeft = ($(window).width() - $('.cd-quick-view').width()) / 2,
          quickViewTop = ($(window).height() - $('.cd-quick-view').height()) / 2;
      $('.cd-quick-view').css({
        "top": quickViewTop,
        "left": quickViewLeft,
      });
    }

    function closeQuickView(id, finalWidth, maxQuickWidth) {
      var close = $('.cd-close'),
          activeSliderUrl = close.siblings('.cd-slider-wrapper').find('.selected img').attr('src'),
          selectedImage = $('.empty-box').find('img').eq(0);
      //update the image in the gallery
      if (!$('.cd-quick-view').hasClass('velocity-animating') && $('.cd-quick-view').hasClass('add-content')) {
        selectedImage.attr('src', activeSliderUrl);
        animateQuickView(id, selectedImage, finalWidth, maxQuickWidth, 'close');
      } else {
        closeNoAnimation(id, selectedImage, finalWidth, maxQuickWidth);
      }
    }

    /*Open Quick View*/
    function animateQuickView(id, image, finalWidth, maxQuickWidth, animationType) {
      //store some image data (width, top position, ...)
      //store window data to calculate quick view panel position
      var parentListItem = image.parent('.image-wrap'),
          topSelected = image.offset().top - $(window).scrollTop(),
          leftSelected = image.offset().left,
          widthSelected = image.width(),
          heightSelected = image.height(),
          windowWidth = $(window).width(),
          windowHeight = $(window).height(),
          finalLeft = (windowWidth - finalWidth) / 2,
          finalHeight = finalWidth * heightSelected / widthSelected,
          finalTop = (windowHeight - finalHeight) / 2,
          quickViewWidth = (windowWidth * .8 < maxQuickWidth) ? windowWidth * .8 : maxQuickWidth,
          quickViewLeft = (windowWidth - quickViewWidth) / 2;

      if (animationType == 'open') {
        //hide the image in the gallery
        parentListItem.addClass('empty-box');
        //place the quick view over the image gallery and give it the dimension of the gallery image
        $(id).css({
          "top": topSelected,
          "left": leftSelected,
          "width": widthSelected,
        }).velocity({
          //animate the quick view: animate its width and center it in the viewport
          //during this animation, only the slider image is visible
          'top': finalTop + 'px',
          'left': finalLeft + 'px',
          'width': finalWidth + 'px',
        }, 1000, [400, 20], function () {
          //animate the quick view: animate its width to the final value
          $(id).addClass('animate-width').velocity({
            'left': quickViewLeft + 'px',
            'width': quickViewWidth + 'px',
          }, 300, 'ease', function () {
            //show quick view content
            $(id).addClass('add-content');
          });
        }).addClass('is-visible');
      } else {
        //close the quick view reverting the animation
        $(id).removeClass('add-content').velocity({
          'top': finalTop + 'px',
          'left': finalLeft + 'px',
          'width': finalWidth + 'px',
        }, 300, 'ease', function () {
          $('body').removeClass('overlay-layer');
          $(id).removeClass('animate-width').velocity({
            "top": topSelected,
            "left": leftSelected,
            "width": widthSelected,
          }, 500, 'ease', function () {
            $(id).removeClass('is-visible');
            parentListItem.removeClass('empty-box');
          });
        });
      }
    }

    /*Close Quick View*/
    function closeNoAnimation(id, image, finalWidth, maxQuickWidth) {
      var parentListItem = image.parent('.image-wrap'),
          topSelected = image.offset().top - $(window).scrollTop(),
          leftSelected = image.offset().left,
          widthSelected = image.width();

      $('body').removeClass('overlay-layer');
      parentListItem.removeClass('empty-box');

      id.velocity("stop").removeClass('add-content animate-width is-visible').css({
        "top": topSelected,
        "left": leftSelected,
        "width": widthSelected,
      });
    }

  }

  setColumns(value: string){
    this.columnsClass = value;
  }

  setItemView(value: string){
    this.itemView = value
  }
}
