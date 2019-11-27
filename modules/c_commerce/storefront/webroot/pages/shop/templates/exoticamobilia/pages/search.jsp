

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<script type="text/html" id="productBoxTemplate">
    {{#products}}
    <div itemscope itemtype="http://schema.org/Enumeration" class="col-md-4 col-sm-6 productItem mb-4 pt-1 d-flex"
         item-order="{{sortOrder}}" item-name="{{description.name}}" item-price="{{price}}" data-id="{{id}}">
        <div class="product-grid6 d-flex flex-column align-content-between justify-content-between">
            <div class="product-image6">
                <a href="<c:url value="/shop/product/" />{{description.friendlyUrl}}.html<sm:breadcrumbParam/>">
                    {{#image}}
                    <img class="pic-1" src="{{image.imageUrl}}"/>
                    {{/image}}
                </a>
            </div>
            <div class="product-content">
                <h4 ><a href="<c:url value="/shop/product/" />{{description.friendlyUrl}}.html<sm:breadcrumbParam/>">{{description.name}}</a></h4>
                {{#discounted}}
                <div class="price">{{finalPrice}}
                    <span>{{originalPrice}}</span>
                </div>
                {{/discounted}}
                {{^discounted}}
                <div class="price">{{finalPrice}}</div>
                {{/discounted}}
            </div>
            <ul class="social">
                <li><a href="<c:url value="/shop/product/" />{{description.friendlyUrl}}.html<sm:breadcrumbParam/>" data-tip="<s:message code="button.label.view" text="Details"/>"><i class="fa fa-search"></i></a></li>
                <%--                <li><a href="" data-tip="Add to Wishlist"><i class="fa fa-shopping-bag"></i></a></li>--%>
                {{#canBePurchased}}
                <li><a href="javascript:void(0);" data-tip="Add to Cart"><i class="addToCart fa fa-shopping-cart" productId="{{uuid}}"></i></a></li>
                {{/canBePurchased}}
            </ul>
        </div>
    </div>
    {{/products}}
</script>


<script>

var START_COUNT_PRODUCTS = 0;
var MAX_PRODUCTS = 18;


$(function(){

search();

});


function search() {
//Invoke search service
$('#productsContainer').showLoading();
var url = '${pageContext.servletContext.contextPath}/services/public/search/<c:out
        value="${requestScope.MERCHANT_STORE.code}"/>/<c:out
        value="${requestScope.LANGUAGE.iso}"/>/' + START_COUNT_PRODUCTS + '/' + MAX_PRODUCTS + '/search.json';
searchProducts(url,'#productsContainer','<c:out value="${q}"/>',null);
}

//inviked from callback below
function buildProductsList(productList) {
log('Products-> ' + productList.products.length);
var productsTemplate = Hogan.compile(document.getElementById("productBoxTemplate").innerHTML);
var productsRendred = productsTemplate.render(productList);
$('#productsContainer').append(productsRendred);
//$('#hiddenProductsContainer').append(productsRendred);//used for filtering products but no filter in search
initBindings();//add to cart etc...
}

//once the list of product is retrieved
function callBackSearchProducts(productList) {
buildProductsList(productList);
totalCount = productList.productCount;
START_COUNT_PRODUCTS = START_COUNT_PRODUCTS + MAX_PRODUCTS;
if(START_COUNT_PRODUCTS < totalCount) {
$("#button_nav").show();
} else {
$("#button_nav").hide();
}

$('#productsContainer').hideLoading();


var productQty = productList.productCount + ' <s:message code="label.search.items.found" text="item(s) found"/>';
$('#products-qty').html(productQty);

//facets
if(productList.categoryFacets!=null && productList.categoryFacets.length>0) {
$('#categoryLabel').show();
for (var i = 0; i < productList.categoryFacets.length; i++) {
var categoryFacets = '<li>';
categoryFacets = categoryFacets + '<a href="<c:url
        value="/shop"/>/category/' + productList.categoryFacets[i].description.friendlyUrl + '.html">' + productList.categoryFacets[i].description.name;
if(productList.categoryFacets[i].productCount>0) {
categoryFacets = categoryFacets + '&nbsp;<span class="countItems">(' + productList.categoryFacets[i].productCount + ')</span>'
}
categoryFacets = categoryFacets + '</a>';
categoryFacets = categoryFacets + '</li>';
$(categoriesFacets).append(categoryFacets);
}
} else {
$('#categoryLabel').hide();
}


}


</script>


<div id="mainContent" class="container">

    <div id="shop" class="row">


        <div class="col-md-9">

            <div class="row top-shop-option">
                <div class="col-sm-9 col-md-9">
                    <strong>
                        <div id="products-qty"></div>
                    </strong>
                </div>
            </div>


            <div class="row product-list">


                <!-- just copy that block for having products displayed -->
                <!-- products are loaded by ajax -->
                <ul id="productsContainer" class="list-unstyled"></ul>

                <nav id="button_nav" style="text-align:center;display:none;">
                    <button id="moreProductsButton" class="btn btn-large" style="width:400px;"
                            onClick="loadCategoryProducts();"><s:message code="label.product.moreitems"
                                                                         text="Display more items"/>...
                    </button>
                </nav>
                <span id="end_nav" style="display:none;"><s:message code="label.product.nomoreitems"
                                                                    text="No more items to be displayed"/></span>
                <!-- end block -->

            </div>

        </div><!-- /col-md-9 -->

        <sidebar class="col-md-3">
            <h3 id="categoryLabel"><s:message code="label.categories.title" text="Categories"/></h3>
            <ul id="categoriesFacets" class="nav nav-list"></ul>
        </sidebar>


    </div><!-- row -->

</div>
<!-- container -->