<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ page session="false" %>


<div class="tabbable">

    <jsp:include page="/common/adminTabs.jsp"/>
    <div id="messages" class="alert alert-info" style="display:none">
    </div>

    <div class="box">
						<span class="box-title">
						<p><s:message code="label.store.information" text="Store information"/></p>
						</span>

        <p>
        <address>
            <strong><c:out value="${store.storeName}"/></strong><br/>
            <c:if test="${not empty store.storeAddress}">
                <c:out value="${store.storeAddress}"/><br/>
            </c:if>
            <c:if test="${not empty store.storeCity}">
                <c:out value="${store.storeCity}"/>,
            </c:if>
            <c:choose>
                <c:when test="${not empty store.zone}">
                    <c:out value="${store.zone.code}"/>,
                </c:when>
                <c:otherwise>
                    <c:if test="${not empty store.storeStateProvince}">
                        <c:out value="${store.storeStateProvince}"/>,
                    </c:if>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty store.storePostalCode}">
                <c:out value="${store.storePostalCode}"/>
            </c:if>
            <br/><c:out value="${country.name}"/>
            <c:if test="${not empty store.storePhone}">
                <br/><c:out value="${store.storePhone}"/>
            </c:if>
        </address>


        </p>
        <p>
            <i class="icon-user"></i>
            <sec:authentication property="principal.username"/><br/>
            <i class="icon-calendar"></i> <s:message code="label.profile.lastaccess" text="Last access"/>:
            <fmt:formatDate type="both" dateStyle="long" value="${user.lastAccess}"/>
        </p>


    </div>

    <sec:authorize access="hasAnyRole('ADMIN', 'SUPERADMIN', 'ADMIN_ORDER')">

        <br/>
        <h3><s:message code="label.order.recent" text="Recent orders"/></h3>
        <br/><br/>

        <!-- Listing grid include -->
        <c:set value="/admin/orders/paging.html?_endRow=10" var="pagingUrl" scope="request"/>
        <c:set value="/admin/orders/remove.html" var="removeUrl" scope="request"/>
        <c:set value="/admin/orders/editOrder.html" var="editUrl" scope="request"/>
        <c:set value="/admin/orders/list.html" var="afterRemoveUrl" scope="request"/>
        <c:set var="entityId" value="orderId" scope="request"/>
        <c:set var="componentTitleKey" value="label.order.title" scope="request"/>
        <c:set var="gridHeader" value="/pages/admin/orders/orders-gridHeader.jsp" scope="request"/>
        <c:set var="canRemoveEntry" value="false" scope="request"/>

        <jsp:include page="/pages/admin/components/list.jsp"></jsp:include>
        <!-- End listing grid include -->

    </sec:authorize>

</div>