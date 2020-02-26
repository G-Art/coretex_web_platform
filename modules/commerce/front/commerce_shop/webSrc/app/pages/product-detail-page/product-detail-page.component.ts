import {Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ProductService} from "../../core/service/product.service";
import {ProductDetailData} from "../../core/data/product-detail.data";
import {ProductVariantData} from "../../core/data/product-variant.data";
import {Location} from "@angular/common";
import {ProductImageSliderComponent} from "../../shared/components/product-image-slider/product-image-slider.component";

declare var $: any;

@Component({
    selector: 'app-product-detail-page',
    templateUrl: './product-detail-page.component.html',
    styleUrls: ['./product-detail-page.component.scss']
})
export class ProductDetailPageComponent implements OnInit {

    @ViewChild("imageContainer", {static: true, read: ViewContainerRef}) imageContainer: ViewContainerRef;

    productDetail: ProductDetailData;

    productDetailVariant: ProductVariantData;

    productStyleVariant: ProductVariantData;

    constructor(private activatedRoute: ActivatedRoute,
                private productService: ProductService,
                private router: Router,
                private location: Location,
                private componentFactoryResolver: ComponentFactoryResolver) {
    }

    ngOnInit() {
        this.activatedRoute.params.subscribe(routeParams => {
            this.productService.getProductDetail(routeParams.code)
                .subscribe(data => {
                    this.productDetail = data;
                    let variants = this.productDetail.variants;
                    if (routeParams.vcode) {
                        this.defineVariants(variants, routeParams.vcode);
                        this.createInitImageViewContainer(this.productStyleVariant);
                    } else {
                        this.defineVariants(variants, null);
                        this.location.replaceState(`/product/${this.productDetail.code}/v/${this.productDetailVariant.code}`);
                    }

                });
        });
    }

    private defineVariants(variants: ProductVariantData[], vcode: string) {
        if (variants.length > 0) {
            if (!vcode) {
                this.productStyleVariant = variants[0];
                this.productDetailVariant = this.productStyleVariant.variants.length > 0 ? this.productStyleVariant.variants[0] : variants[0];
            }

            for (let v of variants) {
                if (v.code == vcode) {
                    this.productStyleVariant = v;
                    this.productDetailVariant = v;
                } else if (v.variants.length > 0) {
                    let size = v.variants.find(v => v.code == vcode);
                    if(size){
                        this.productStyleVariant = v;
                        this.productDetailVariant = size;
                    }
                }
            }
        }
    }

    changeColorVariant(colorVariant: ProductVariantData) {
        this.productStyleVariant = colorVariant;
        this.productDetailVariant = this.productStyleVariant.variants.length > 0 ? this.productStyleVariant.variants[0] : colorVariant;
        this.location.replaceState(`/product/${this.productDetail.code}/v/${this.productDetailVariant.code}`);
        this.createInitImageViewContainer(this.productStyleVariant);
    }

    changeSizeVariant(sizeCode: string) {
        this.productDetailVariant = this.productStyleVariant.variants.find(value => value.code == sizeCode);
        this.location.replaceState(`/product/${this.productDetail.code}/v/${this.productDetailVariant.code}`);
    }

    createInitImageViewContainer(style: ProductVariantData){
        this.imageContainer.clear();
        let componentFactory = this.componentFactoryResolver.resolveComponentFactory(ProductImageSliderComponent);
        let productImageSliderComponentComponentRef = this.imageContainer.createComponent(componentFactory);
        productImageSliderComponentComponentRef.instance.productStyleVariant = style;
    }

    ngAfterViewInit() {
        this.init();
    }
    private init() {

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


    }


}
