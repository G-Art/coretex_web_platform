/*global jQuery */
(function ($) {
    "use strict";

    jQuery(document).ready(function ($) {

        $(window).on('scroll', function () {
            // Menu Bind to scroll
            var fromTop = $(this).scrollTop() + topMenuHeight;
            var cur = scrollItems.map(function () {
                if ($(this).offset().top < fromTop)
                    return this;
            });
            cur = cur[cur.length - 1];
            var id = cur && cur.length ? cur[0].id : "";
            if (lastId !== id) {
                lastId = id;
                menuItems
                    .parent().removeClass("active")
                    .end().filter("[href='#" + id + "']").parent().addClass("active");
            }
        });

        /*--------------------------------
         Nav menu active class change
        -------------------------------- */
        var lastId,
            topMenu = $(".main-menu"),
            topMenuHeight = topMenu.outerHeight() + 15,
            menuItems = topMenu.find("a"),
            scrollItems = menuItems.map(function () {
                var item = $($(this).attr("href"));
                if (item.length) {
                    return item;
                }
            });


        /*---------------------
         Smooth Scroll
        --------------------- */
        $('.main-menu a[href*="#"]:not([href="#"])').on("click", function () {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html, body').animate({
                        scrollTop: target.offset().top
                    }, 700);
                    return false;
                }
            }
        });

    }); //Ready Function End

    jQuery(window).on('load', function () {

    }); //window load End


}(jQuery));
