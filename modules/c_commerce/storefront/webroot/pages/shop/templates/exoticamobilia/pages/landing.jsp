

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>


<div class="main">
    <c:if test="${page!=null}">
        <div id="" class="container">
            <c:out value="${page.description}" escapeXml="false"/>
        </div>
    </c:if>


    <br/>

    <div id="" class="container">
        <c:if test="${requestScope.FEATURED_ITEM!=null || requestScope.SPECIALS!=null}">
            <!-- one div by section -->
            <c:if test="${requestScope.FEATURED_ITEM!=null}">
                <h2 class="hTitle"><s:message code="menu.catalogue-featured" text="Featured items"/></h2>
                <!-- Iterate over featuredItems -->
                <c:set var="ITEMS" value="${requestScope.FEATURED_ITEM}" scope="request"/>
                <c:set var="FEATURED" value="true" scope="request"/>
                <jsp:include page="/pages/shop/templates/exoticamobilia/sections/productBox.jsp"/>
            </c:if>
            <c:if test="${requestScope.SPECIALS!=null}">
                <h2 class="hTitle"><s:message code="label.product.specials" text="Specials"/></h2>
                <!-- Iterate over featuredItems -->
                <c:set var="ITEMS" value="${requestScope.SPECIALS}" scope="request"/>
                <jsp:include page="/pages/shop/templates/exoticamobilia/sections/productBox.jsp"/>
            </c:if>
        </c:if>
    </div>

    <div class="container">

    </div>


</div>
</div>