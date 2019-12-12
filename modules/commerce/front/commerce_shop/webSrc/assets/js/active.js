(function ($) {
    "use strict";


    
    /*=============================================
    =            variables            =
    =============================================*/
    
    var $window = $(window),
        $html = $('html'),
        $body = $('body'),
        $headerSticky = $('.header-sticky');
    
    /*=====  End of variables  ======*/
    

    
    /*=============================================
    =            sticky header            =
    =============================================*/
    
    $window.on('scroll', function() {
        if ($window.scrollTop() >= 200 && $window.width() > 767) {
            $headerSticky.addClass('is-sticky');
        }
        else {
            $headerSticky.removeClass('is-sticky');
        }

        //code for scroll top
        var scroll = $window.scrollTop();

		if (scroll >= 400) {
			$('.scroll-top').fadeIn();
		} else {
			$('.scroll-top').fadeOut();
		}

    });
    
    /*=====  End of sticky header  ======*/

    
    /*=============================================
    =            scroll top            =
    =============================================*/
    
    $('.scroll-top').on('click', function () {
		$('html,body').animate({
			scrollTop: 0
		}, 2000);
	});
    
    /*=====  End of scroll top  ======*/


    
    /*=============================================
    =     active and deactive search overlay            =
    =============================================*/
    
    $('#search-icon, #search-icon-2').on('click', function(){
		$('#search-overlay').addClass('active-search-overlay');
		$('body').addClass('active-body-search-overlay');
	});

	$('#search-close-icon').on('click', function(){
		$('#search-overlay').removeClass('active-search-overlay');
		$('body').removeClass('active-body-search-overlay');
	});

    
    /*=====  End of active and deactive search overlay  ======*/
    
    
    
    


    
    /*=============================================
    =            offcanvas menu            =
    =============================================*/
    
    var $offCanvasNav = $('.offcanvas-naviagtion'),
        $offCanvasNavSubMenu = $offCanvasNav.find('.sub-menu');
    
    /*Add Toggle Button With Off Canvas Sub Menu*/
    $offCanvasNavSubMenu.parent().prepend('<span class="menu-expand"><i></i></span>');
    
    /*Close Off Canvas Sub Menu*/
    $offCanvasNavSubMenu.slideUp();
    
    /*Category Sub Menu Toggle*/
    $offCanvasNav.on('click', 'li a, li .menu-expand', function(e) {
        var $this = $(this);
        if ( ($this.parent().attr('class').match(/\b(menu-item-has-children|has-children|has-sub-menu)\b/)) && ($this.attr('href') === '#' || $this.hasClass('menu-expand')) ) {
            e.preventDefault();
            if ($this.siblings('ul:visible').length){
                $this.parent('li').removeClass('active');
                $this.siblings('ul').slideUp();
            } else {
                $this.parent('li').addClass('active');
                $this.closest('li').siblings('li').removeClass('active').find('li').removeClass('active');
                $this.closest('li').siblings('li').find('ul:visible').slideUp();
                $this.siblings('ul').slideDown();
            }
        }
    });
    
    /*=====  End of offcanvas menu  ======*/


    
    /*=============================================
    =            mobile menu activation            =
    =============================================*/
    
    $('#mobile-menu-trigger').on('click', function(){
        $('#offcanvas-mobile-menu').removeClass('inactive').addClass('active');
    });
    
    
    $('#offcanvas-menu-close-trigger').on('click', function(){
        $('#offcanvas-mobile-menu').removeClass('active').addClass('inactive');
    });
    
    /*=====  End of mobile menu activation  ======*/
    
    
    
    
    
    /*=============================================
    =            mailchimp            =
    =============================================*/
    
    $('#mc-form').ajaxChimp({
		language: 'en',
		callback: mailChimpResponse,
		// ADD YOUR MAILCHIMP URL BELOW HERE!
		url: 'http://devitems.us11.list-manage.com/subscribe/post?u=6bbb9b6f5827bd842d9640c82&amp;id=05d85f18ef'

	});

	function mailChimpResponse(resp) {

		if (resp.result === 'success') {
			$('.mailchimp-success').html('' + resp.msg).fadeIn(900);
			$('.mailchimp-error').fadeOut(400);

		} else if (resp.result === 'error') {
			$('.mailchimp-error').html('' + resp.msg).fadeIn(900);
		}
	}
    
    $('#mc-form2').ajaxChimp({
		language: 'en',
		callback: mailChimpResponse2,
		// ADD YOUR MAILCHIMP URL BELOW HERE!
		url: 'http://devitems.us11.list-manage.com/subscribe/post?u=6bbb9b6f5827bd842d9640c82&amp;id=05d85f18ef'

	});

	function mailChimpResponse2(resp) {

		if (resp.result === 'success') {
			$('.mailchimp-success2').html('' + resp.msg).fadeIn(900);
			$('.mailchimp-error2').fadeOut(400);

		} else if (resp.result === 'error') {
			$('.mailchimp-error2').html('' + resp.msg).fadeIn(900);
		}
	}

    
    /*=====  End of mailchimp  ======*/
    
    
    

    /*=============================================
    =            perfect scrollbar            =
    =============================================*/
    
    $('.ps-scroll').each(function() {
		if($('.ps-scroll').length) {
			const ps = new PerfectScrollbar($(this)[0]);
		}
	});
    
    /*=====  End of perfect scrollbar  ======*/
    

    
    /*=============================================
    =            masonry activation            =
    =============================================*/
    
    // banner masonry
    var $masonryLayout1 = $('.masonry-layout--banner');
		var $masonryItem1 = $('.masonry-item--banner');
        $masonryItem1.hide();
        
		$masonryLayout1.imagesLoaded( function() {
			$masonryItem1.fadeIn();
			$masonryLayout1.masonry({
				itemSelector: '.masonry-item--banner',
				percentPosition: true
			});
		});
    

    // category masonry
    var $masonryLayout2 = $('.masonry-layout--category');
		var $masonryItem2 = $('.masonry-item--category');
        $masonryItem2.hide();
        
		$masonryLayout2.imagesLoaded( function() {
			$masonryItem2.fadeIn();
			$masonryLayout2.masonry({
                itemSelector: '.masonry-item--category',
                columnWidth: '.grid-sizer',
				percentPosition: true
			});
		});
		
    
    /*=====  End of masonry activation  ======*/
    
    
    
    /*=============================================
    =            countdown activation            =
    =============================================*/
    
    $('[data-countdown]').each(function () {
		var $this = $(this),
		finalDate = $(this).data('countdown');
		$this.countdown(finalDate, function (event) {
			$this.html(event.strftime('<div class="single-countdown"><span class="single-countdown__time">%D</span><span class="single-countdown__text">Days</span></div><div class="single-countdown"><span class="single-countdown__time">%H</span><span class="single-countdown__text">Hours</span></div><div class="single-countdown"><span class="single-countdown__time">%M</span><span class="single-countdown__text">Minutes</span></div><div class="single-countdown"><span class="single-countdown__time">%S</span><span class="single-countdown__text">Seconds</span></div>'));
		});
	});

    
    /*=====  End of countdown activation  ======*/
    
    
    /*=============================================
    =            instafeed activation            =
    =============================================*/
    
    var activeId = $("#instafeed"),
	myTemplate = '<div class="col"><div class="instagram-item"><a href="{{link}}" target="_blank" id="{{id}}"><img src="{{image}}" /></a><div class="instagram-hvr-content"><span class="tottallikes"><i class="pe-7s-like"></i>{{likes}}</span><span class="totalcomments"><i class="pe-7s-comment"></i>{{comments}}</span></div></div></div>';

	if (activeId.length) {
		var userID = activeId.attr('data-userid'),
			accessTokenID = activeId.attr('data-accesstoken'),

			userFeed = new Instafeed({
				get: 'user',
				userId: userID,
				accessToken: accessTokenID,
				resolution: 'standard_resolution',
				template: myTemplate,
				sortBy: 'least-recent',
				limit: 10,
				links: false
			});
		userFeed.run();
	}
    
    /*=====  End of instafeed activation  ======*/


    
    /*=============================================
    =            slick slider active            =
    =============================================*/
    
    var $themeSlickSlider = $('.theme-slick-slider');
    
    /*For RTL*/
    if( $html.attr("dir") == "rtl" || $body.attr("dir") == "rtl" ){
        $themeSlickSlider.attr("dir", "rtl");
    }
    
    $themeSlickSlider.each(function(){
        
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
    =            custom quick view            =
    =============================================*/
    
    
	//final width --> this is the quick view image slider width
	//maxQuickWidth --> this is the max-width of the quick-view panel
	var sliderFinalWidth = 400,
    maxQuickWidth = 900;

    //open the quick view panel
    $('.cd-trigger').on('click', function(event){
        event.preventDefault();
        var selectedImage = $(this).closest('.single-grid-product, .single-list-product').find('.single-grid-product__image .image-wrap, .single-list-product__image .image-wrap').children('img').eq(0),
            id = $(this).attr('href'),
            slectedImageUrl = selectedImage.attr('src');

        $('body').addClass('overlay-layer');
        animateQuickView(id, selectedImage, sliderFinalWidth, maxQuickWidth, 'open');

        //update the visible slider image in the quick view panel
        //you don't need to implement/use the updateQuickView if retrieving the quick view data with ajax
        //updateQuickView(slectedImageUrl);
    });

    //close the quick view panel
    $('body').on('click', function(event){
        if( $(event.target).is('.cd-close') || $(event.target).is('body.overlay-layer')) {
            event.preventDefault();
            var id = $(this).find('.cd-quick-view.add-content');
            closeQuickView(id, sliderFinalWidth, maxQuickWidth);
        }
    });
    
    $(document).keyup(function(event){
        //check if user has pressed 'Esc'
        if(event.which=='27'){
            var id = $('body').find('.cd-quick-view.add-content');
            closeQuickView(id, sliderFinalWidth, maxQuickWidth);
        }
    });

    //quick view slider Update On Navigation
    $('.cd-quick-view').on('click', '.cd-slider-navigation a', function(event){
        event.preventDefault();
        var $this = $(this);
        updateSliderNav($this);
    });

    //quick view slider Update On Pagination
    $('.cd-quick-view').on('click', '.cd-slider-pagination a', function(event){
        event.preventDefault();
        var $this = $(this),
            $li = $this.parents('li');
        
        $li.addClass('active').siblings().removeClass('active');
        
        updateSliderPage($this);
    });

    //center quick-view on window resize
    $(window).on('resize', function(){
        if($('.cd-quick-view').hasClass('is-visible')){
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
        if ( navigation.hasClass('cd-next') ) {
            if( !activeSlider.is(':last-child') ) { 
                activeSlider.next().addClass('selected');
                sliderPanilation.children('li').eq(activeSlider.next().index()).addClass('active');
            } else {
                sliderConatiner.children('li').eq(0).addClass('selected');
                sliderPanilation.children('li').eq(sliderConatiner.children('li').eq(0).index()).addClass('active');
            } 
        } else {
            if( !activeSlider.is(':first-child') ) { 
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

    //    /*Update Quick View Slide Item*/
    //	function updateQuickView(url) {
    //		$('.cd-quick-view .cd-slider li').removeClass('selected').find('img[src="'+ url +'"]').parent('li').addClass('selected');
    //	}

    /*Resize Quick View*/
    function resizeQuickView() {
        var quickViewLeft = ($(window).width() - $('.cd-quick-view').width())/2,
            quickViewTop = ($(window).height() - $('.cd-quick-view').height())/2;
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
        if( !$('.cd-quick-view').hasClass('velocity-animating') && $('.cd-quick-view').hasClass('add-content')) {
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
            finalLeft = (windowWidth - finalWidth)/2,
            finalHeight = finalWidth * heightSelected/widthSelected,
            finalTop = (windowHeight - finalHeight)/2,
            quickViewWidth = ( windowWidth * .8 < maxQuickWidth ) ? windowWidth * .8 : maxQuickWidth ,
            quickViewLeft = (windowWidth - quickViewWidth)/2;

        if( animationType == 'open') {
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
                'top': finalTop+ 'px',
                'left': finalLeft+'px',
                'width': finalWidth+'px',
            }, 1000, [ 400, 20 ], function(){
                //animate the quick view: animate its width to the final value
                $(id).addClass('animate-width').velocity({
                    'left': quickViewLeft+'px',
                    'width': quickViewWidth+'px',
                }, 300, 'ease' ,function(){
                    //show quick view content
                    $(id).addClass('add-content');
                });
            }).addClass('is-visible');
        } else {
            //close the quick view reverting the animation
            $(id).removeClass('add-content').velocity({
                'top': finalTop+ 'px',
                'left': finalLeft+'px',
                'width': finalWidth+'px',
            }, 300, 'ease', function(){
                $('body').removeClass('overlay-layer');
                $(id).removeClass('animate-width').velocity({
                    "top": topSelected,
                    "left": leftSelected,
                    "width": widthSelected,
                }, 500, 'ease', function(){
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

    
    /*=====  End of custom quick view  ======*/



    
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
    =            video background active            =
    =============================================*/
    
    var videoBg = $(".video-bg");

	videoBg.each(function (index, elem) {
		var element = $(elem),
			videoUrl = element.data('url');

		videoBg.YTPlayer({
			videoURL: videoUrl,
			showControls: false,
			showYTLogo: false,
			mute: true,
			quality: 'highres',
			containment: '.video-area',
			ratio: 'auto'
		});
	});
    
    /*=====  End of video background active  ======*/


    
    /*=============================================
    =            price filter active            =
    =============================================*/
    
    $('#price-range').slider({
		range: true,
		min: 25,
		max: 350,
		values: [ 25, 350 ],
		slide: function( event, ui ) {
			$('#price-amount').val( 'Price: ' + '$' + ui.values[ 0 ] + ' - $' + ui.values[ 1 ] );
		}
	});
	$('#price-amount').val( 'Price: ' + '$' + $('#price-range').slider( 'values', 0 ) +
		' - $' + $('#price-range').slider('values', 1 ) ); 

    
    /*=====  End of price filter active  ======*/
    

    /*=============================================
    =            sidebar dropdown            =
    =============================================*/
    
    $('#single-sidebar-widget__dropdown li.has-children').append('<i class="fa fa-angle-down"></i>');

    $('#single-sidebar-widget__dropdown li.has-children > i').on('click', function(){
        $(this).parent().toggleClass('active');

        $(this).siblings('.sub-menu').slideToggle();
    });
    
    /*=====  End of sidebar dropdown  ======*/


    
    /*=============================================
    =            product view mode changer            =
    =============================================*/
    
    $('.view-mode-icons a').on('click', function (e) {
		e.preventDefault();

		var shopProductWrap = $('.shop-product-wrap');
        var viewMode = $(this).data('target');
        
        if(viewMode == 'list'){
            $('#grid-view-changer').hide();
        }else{
            $('#grid-view-changer').show();
        }

		$('.view-mode-icons a').removeClass('active');
		$(this).addClass('active');
		shopProductWrap.removeClass('grid list').addClass(viewMode);
	});

    
    /*=====  End of product view mode changer  ======*/


    
    /*=============================================
    =            product grid view changer            =
    =============================================*/
    
    $('#grid-view-change-trigger').on('click', function(e){
        e.preventDefault();

        $(this).parent().addClass('active');
    })
    
    $('#grid-view-close-trigger').on('click', function(e){
        e.preventDefault();
        $('#grid-view-changer').removeClass('active');
    })

    $('#grid-view-changer__menu a').on('click', function(e){
        e.preventDefault();
        var shopProductWrap = $('.shop-product-wrap');
        var viewMode = $(this).data('target');

        $('#grid-view-changer__menu a').removeClass('active');
        $(this).addClass('active');
        shopProductWrap.removeClass('two-column three-column four-column').addClass(viewMode);

        if(viewMode == 'two-column'){
			shopProductWrap.children().addClass('col-lg-6').removeClass('col-lg-4 col-lg-3 col-lg-is-5 col-lg-is-6');
		}

		if(viewMode == 'three-column'){
			shopProductWrap.children().addClass('col-lg-4').removeClass('col-lg-3 col-lg-6 col-lg-is-5 col-lg-is-6');
		}

		if(viewMode == 'four-column'){
			shopProductWrap.children().addClass('col-lg-3').removeClass('col-lg-4 col-lg-6 col-lg-is-5 col-lg-is-6');
		}

		if(viewMode == 'five-column'){
			shopProductWrap.children().addClass('col-lg-is-5').removeClass('col-lg-3 col-lg-4 col-lg-6 col-lg-is-6');
		}

		if(viewMode == 'six-column'){
			shopProductWrap.children().addClass('col-lg-is-6').removeClass('col-lg-3 col-lg-4 col-lg-6 col-lg-is-5');
		}
    });
    
    /*=====  End of product grid view changer  ======*/
    
    
    
    
    /*=============================================
    =            nice select activation            =
    =============================================*/
    
    $('.nice-select').niceSelect();
    
    /*=====  End of nice select activation  ======*/

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
    
    
    
    /*=============================================
    =            payment method select            =
    =============================================*/
    
    $('[name="payment-method"]').on('click', function () {

        var $value = $(this).attr('value');

        $('.single-method p').slideUp();
        $('[data-method="' + $value + '"]').slideDown();

    });
    
    /*=====  End of payment method select  ======*/


    
    /*=============================================
    =            shipping form active            =
    =============================================*/
    
    $('[data-shipping]').on('click', function () {
        if ($('[data-shipping]:checked').length > 0) {
            $('#shipping-form').slideDown();
        } else {
            $('#shipping-form').slideUp();
        }
    });
    
    /*=====  End of shipping form active  ======*/
    
    

})(jQuery);