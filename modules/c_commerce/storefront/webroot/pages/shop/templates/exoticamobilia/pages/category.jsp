<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<script src="<c:url value="/resources/js/jquery.easing.1.3.js" />"></script>
<script src="<c:url value="/resources/js/jquery.quicksand.js" />"></script>
<script src="<c:url value="/resources/js/jquery-sort-filter-plugin.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>


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
    var MAX_PRODUCTS = 30;
    var filter = null;
    var filterValue = null;

    $(function () {

        //price minimum/maximum
        $('.numeric').numeric();


        $('#filter').on('change', function () {
            visualize();
        });

        $('#priceFilterMinimum').on('blur', function () {
            visualize();
        });

        $('#priceFilterMaximum').on('blur', function () {
            visualize()
        });


        loadCategoryProducts();

    });


    function visualize() {
        var orderBy = $("#filter").val();
        var minimumPrice = $('#priceFilterMinimum').val();
        var maximumPrice = $('#priceFilterMaximum').val();

        //orderProducts(orderBy);
        orderProducts(orderBy, minimumPrice, maximumPrice);
    }

    /** used for ordering and filtering **/
    //function orderProducts(attribute, minimum, maximum) {
    function orderProducts(attribute, minimumPrice, maximumPrice) {

        if (minimumPrice == undefined) {
            minimumPrice = '';
        }

        if (maximumPrice == undefined) {
            maximumPrice = '';
        }

        //log('Attribute ' + attribute + ' Minimum price ' + minimumPrice + ' Maximum price ' + maximumPrice);

        if (minimumPrice == '' && maximumPrice == '') {

            if (attribute == 'item-order') {
                return;
            }
        }

        // get the first collection
        var $prods = $('#productsContainer');


        // clone applications to get a second collection
        data = $('#hiddenProductsContainer').clone();

        //console.log('Data');
        //console.log(data);


        listedData = data.find('.productItem');

        //console.log('Listed Data');
        //console.log(listedData);

        filteredData = listedData;
        var $sortedData = null;

        if (minimumPrice != '' || maximumPrice != '') {
            //filter filteredData
            if (minimumPrice == '') {
                minimumPrice = '0';
            }
            filteredData = listedData.filter(function () {

                //log('Item price ' + $(this).attr('item-price'));

                var price = parseFloat($(this).attr('item-price'));
                if (maximumPrice != '') {
                    return price >= parseFloat(minimumPrice) && price <= parseFloat(maximumPrice);
                } else {
                    return price >= parseFloat(minimumPrice);
                }

            });
        }

        //console.log('After filtered Data');
        //console.log(filteredData);


        if (attribute != 'item-order') {

            $sortedData = filteredData.sorted({
                by: function (v) {
                    if (attribute == 'item-price') {
                        return parseFloat($(v).attr(attribute));
                    } else {
                        return $(v).attr(attribute);
                    }
                }
            });

        } else {
            $sortedData = filteredData;
        }

        // finally, call quicksand
        $prods.quicksand($sortedData, {
            adjustHeight: false,
            adjustWidth: false,
            duration: 800,
            easing: 'easeInOutQuad'
        });


    }

    function loadCategoryProducts() {
        var url = '${pageContext.request.contextPath}/services/public/products/page/' + START_COUNT_PRODUCTS + '/' + MAX_PRODUCTS + '/<c:out value="${requestScope.MERCHANT_STORE.code}"/>/<c:out value="${requestScope.LANGUAGE.code}"/>/<c:out value="${category.description.friendlyUrl}"/>';

        if (filter != null) {
            url = url + '/filter=' + filter + '/filter-value=' + filterValue + '';
        }
        loadProducts(url, '#productsContainer');

    }


    function filterCategory(filterType, filterVal) {
        //reset product section
        $('#productsContainer').html('');
        $('#hiddenProductsContainer').html('');
        START_COUNT_PRODUCTS = 0;
        filter = filterType;
        filterValue = filterVal;
        loadCategoryProducts();
    }

    function buildProductsList(productList, divProductsContainer) {
        log('Products-> ' + productList.products.length);
        var productsTemplate = Hogan.compile(document.getElementById("productBoxTemplate").innerHTML);
        var productsRendred = productsTemplate.render(productList);
        $('#productsContainer').append(productsRendred);
        $('#hiddenProductsContainer').append(productsRendred);
        initBindings();
    }

    function callBackLoadProducts(productList) {
        totalCount = productList.productCount;
        START_COUNT_PRODUCTS = START_COUNT_PRODUCTS + MAX_PRODUCTS;
        if (START_COUNT_PRODUCTS < totalCount && START_COUNT_PRODUCTS <= productList.productCount) {
            $("#button_nav").show();
        } else {
            $("#button_nav").hide();
        }
        $('#productsContainer').hideLoading();

        visualize();

        var productQty = productList.productCount + ' <s:message code="label.search.items.found" text="item(s) found" />';
        $('#products-qty').html(productQty);


    }


</script>


<div id="mainContent" class="container">

    <header class="page-header row pb-4">
        <c:if test="${category.description.name!=null}">
            <div class="dark-translucent-bg ">
                <div class="container">
                    <h2 class="shop-banner-title lead mt-1 mb-0"><c:out value="${category.description.name}"/></h2>
                </div>
            </div>
        </c:if>
        <jsp:include page="/pages/shop/templates/exoticamobilia/sections/breadcrumb.jsp"/>
    </header>

<%--    <div class="container sorting-filters">--%>
<%--        <form>--%>
<%--            <div class="form-group row">--%>
<%--                <div class="col-md-6 ">--%>
<%--                    <div class="row">--%>
<%--                        <label for="filter" class="col-sm-4 col-form-label col-form-label-sm"><s:message code="label.generic.sortby" text="Sort by"/>:</label>--%>
<%--                        <div class="col-sm-5">--%>
<%--                            <select id="filter" class="form-control form-control-sm">--%>
<%--                                <option value="item-order"><s:message code="label.generic.default" text="Default"/></option>--%>
<%--                                <option value="item-name"><s:message code="label.generic.name" text="Name"/></option>--%>
<%--                                <option value="item-price"><s:message code="label.generic.price" text="Price"/></option>--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </form>--%>
<%--    </div>--%>

    <div id="shop" class="row">

        <div class="col-md-9">

            <div class="container product-list">


                <!-- just copy that block for havimg products displayed -->
                <!-- products are loaded by ajax -->
                <div id="productsContainer" class="row list-unstyled"></div>

                <nav id="button_nav" style="text-align:center;display:none;">
                    <button id="moreProductsButton" class="btn btn-primary btn-large" style="width:400px;"
                            onClick="loadCategoryProducts();"><s:message code="label.product.moreitems"
                                                                         text="Display more items"/>...
                    </button>
                </nav>
                <span id="end_nav" style="display:none;"><s:message code="label.product.nomoreitems"
                                                                    text="No more items to be displayed"/></span>
                <!-- end block -->

            </div>

            <!-- hidden -->
            <div id="hiddenProductsContainer" style="display:none;"></div>

        </div><!-- /col-md-9 -->

        <sidebar class="col-md-3">
            <!-- categories -->
            <c:if test="${parent!=null}">
            <h3><c:out value="${parent.description.name}"/></h3>
            </c:if>
            <ul class="nav nav-list">
                <c:forEach items="${subCategories}" var="subCategory">
                    <c:if test="${subCategory.visible}">
                        <li>
                            <a href="<c:url value="/shop/category/${subCategory.description.friendlyUrl}.html"/><sm:breadcrumbParam categoryId="${subCategory.uuid}"/>"><i
                                    class="fa fa-angle-right"></i> <c:out value="${subCategory.description.name}"/>
                                <c:if test="${subCategory.productCount>0}">&nbsp;<span class="countItems">(<c:out
                                        value="${subCategory.productCount}"/>)</span></c:if></a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
            <br/>
            <!-- manufacturers -->
            <c:if test="${fn:length(manufacturers) > 0}">
            <h3><s:message code="label.manufacturer.collection" text="Collection"/></h3>
            <ul class="nav nav-list">
                <li class="nav-header"></li>
                <c:forEach items="${manufacturers}" var="manufacturer">
                    <li>
                        <a href="javascript:filterCategory('BRAND','${manufacturer.uuid}')"><i
                                class="fa fa-angle-right"></i>&nbsp;<c:out
                                value="${manufacturer.name}"/></a></li>
                </c:forEach>
            </ul>
    </div>
    </c:if>


    </sidebar>


</div>
</div>