import {Component, Input, OnInit} from '@angular/core';
import {ProductVariantData} from "../../../core/data/product-variant.data";

declare var $: any;

@Component({
  selector: 'app-product-image-slider',
  templateUrl: './product-image-slider.component.html',
  styleUrls: ['./product-image-slider.component.scss']
})
export class ProductImageSliderComponent implements OnInit {

  @Input()
  productStyleVariant: ProductVariantData;

  constructor() { }

  ngOnInit() {

  }

  ngAfterViewInit() {
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
  }

}
