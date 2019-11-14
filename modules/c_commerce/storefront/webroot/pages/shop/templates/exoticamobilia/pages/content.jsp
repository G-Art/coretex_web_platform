

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


<!-- don't change that script except max_oroducts -->
<script>

    var START_COUNT_PRODUCTS = 0;
    var MAX_PRODUCTS = 500;
    var filter = null;
    var filterValue = null;

    $(function () {

        <c:if test="${productGroup!=null}">
        loadItemsProducts();
        </c:if>

    });


    function loadItemsProducts() {

        //services/public/{store}/products/group/{code}
        var url = '<%=request.getContextPath()%>/services/public/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/products/group/<c:out value="${productGroup}"/>';
        loadProducts(url, '#productsContainer');

    }


    function buildProductsList(productList, divProductsContainer) {
        log('Products-> ' + productList.products.length);
        var productsTemplate = Hogan.compile(document.getElementById("productBoxTemplate").innerHTML);
        var productsRendred = productsTemplate.render(productList);
        $('#productsContainer').append(productsRendred);
        initBindings();
    }

    function callBackLoadProducts(productList) {

        var productQty = productList.productCount + ' <s:message code="label.search.items.found" text="item(s) found" />';
        $('#products-qty').html(productQty);
        $('#productsContainer').hideLoading();

    }

</script>


<div class="container">
    <div class="row">
        <div id="shop" class="col-md-12">
            <c:out value="${content.description}" escapeXml="false"/>
        </div>
    </div>
    <c:if test="${productGroup!=null}">
        <div class="col-md-12">
            <div class="product-list">


                <!-- just copy that block for havimg products displayed -->
                <!-- products are loaded by ajax -->
                <div id="productsContainer" class="list-unstyled"></div>

                <!-- end block -->

            </div>

        </div>
    </c:if>
</div>