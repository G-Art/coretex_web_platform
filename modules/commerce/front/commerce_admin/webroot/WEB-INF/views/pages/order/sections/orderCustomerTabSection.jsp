<%--@elvariable id="order" type="com.coretex.commerce.data.AbstractOrderData"--%>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="tags-order" tagdir="/WEB-INF/tags/common/order" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${not empty order.customer}">
        <tags-order:orderCustomerInfo customer="${order.customer}"/>
    </c:when>
    <c:otherwise>
        <tags-common:annonimus/>
    </c:otherwise>
</c:choose>
