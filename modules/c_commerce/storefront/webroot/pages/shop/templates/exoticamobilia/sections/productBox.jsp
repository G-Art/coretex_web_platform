<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>
<div class="row-exoticamobilia row">

    <c:forEach items="${requestScope.ITEMS}" var="product">
        <div itemscope itemtype="http://schema.org/Enumeration" class="col-md-4 col-sm-6 productItem mb-4 pt-1 d-flex"
             data-id="${product.uuid}" item-price="${product.price}" item-name="${product.description.name}"
             item-order="${product.sortOrder}">
            <div class="product-grid6 d-flex flex-column align-content-between justify-content-between">
                <div class="product-image6">
                    <a href="<c:url value="/shop/product/" /><c:out value="${product.description.friendlyUrl}"/>.html<sm:breadcrumbParam productId="${product.uuid}"/>">
                        <img class="pic-1"
                             src="<sm:shopProductImage imageName="${product.image.imageName}"  sku="${product.sku}"/>"/>
                    </a>
                </div>
                <div class="product-content">
                    <h4>
                        <a href="<c:url value="/shop/product/" /><c:out value="${product.description.friendlyUrl}"/>.html<sm:breadcrumbParam productId="${product.uuid}"/>">${product.description.name}</a>
                    </h4>
                    <c:choose>
                        <c:when test="${product.discounted}">
                            <div class="price"><c:out value="${product.finalPrice}"/>
                                <span><c:out value="${product.originalPrice}"/></span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="price"><c:out value="${product.finalPrice}"/></div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <ul class="social">
                    <li>
                        <a href="<c:url value="/shop/product/" /><c:out value="${product.description.friendlyUrl}"/>.html<sm:breadcrumbParam productId="${product.uuid}"/>"
                           data-tip="<s:message code="button.label.view" text="Details"/>"><i class="fa fa-search"></i></a>
                    </li>
                        <%--                <li><a href="" data-tip="Add to Wishlist"><i class="fa fa-shopping-bag"></i></a></li>--%>

                    <c:choose>
                        <c:when test="${requestScope.FEATURED==true}">
                            <c:if test="${requestScope.CONFIGS['displayAddToCartOnFeaturedItems']==true && requestScope.CONFIGS['allowPurchaseItems']==true && product.canBePurchased}">
                                <li>
                                    <a href="javascript:void(0);"
                                       data-tip="<s:message code="button.label.addToCart" text="Add to cart"/>">
                                        <i class="addToCart fa fa-shopping-cart"
                                           productId="${product.uuid}"></i>
                                    </a>
                                </li>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${requestScope.CONFIGS['allowPurchaseItems']==true  && product.canBePurchased}">
                                <li>
                                    <a href="javascript:void(0);"
                                       data-tip="<s:message code="button.label.addToCart" text="Add to cart"/>">
                                        <i class="addToCart fa fa-shopping-cart"
                                           productId="${product.uuid}"></i>
                                    </a>
                                </li>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </c:forEach>
</div>  