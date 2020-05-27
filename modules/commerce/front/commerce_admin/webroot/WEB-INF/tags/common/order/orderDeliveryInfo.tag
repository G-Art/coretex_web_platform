<%@ taglib prefix="common-components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ attribute name="order" required="true" type="com.coretex.commerce.data.AbstractOrderData" %>

<common-components:cardBlock title="Delivery info"
                             fullCardButton="false"
                             minimizeCardButton="false">
    <jsp:attribute name="cardBlock">

        <c:if test="${not empty order.deliveryType}">
            <c:set var="deliveryType" value="${order.deliveryType}"/>
            <jsp:include page="/fragment/delivery/${deliveryType.deliveryServiceCode}/deliveryInfo.jsp"/>
        </c:if>

    </jsp:attribute>

</common-components:cardBlock>