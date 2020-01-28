(function ($) {
    "use strict";

    /*=============================================
    =            variables            =
    =============================================*/
    
    var $html = $('html'),
        $body = $('body');
    
    /*=====  End of variables  ======*/



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