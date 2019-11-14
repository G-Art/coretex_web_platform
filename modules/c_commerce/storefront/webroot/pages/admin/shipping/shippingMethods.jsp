<%--@elvariable id="deliveryServices" type="java.util.List<com.coretex.core.data.shipping.DeliveryServiceData>"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<div class="tabbable">
    <jsp:include page="/common/adminTabs.jsp"/>
    <div class="tab-content">

        <div class="tab-pane active" id="shipping-methods">
            <div class="sm-ui-component">
                <h3><s:message code="label.shipping.title" text="Shipping configuration"/></h3>
                <br/>


                <div id="store.success" class="alert alert-success" style="
                <c:choose>
                <c:when test="${success!=null}">display:block;</c:when>
                <c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success"
                                                                                text="Request successfull"/></div>

                <div class="control-group">
                    <table class="table table-hover">
                        <c:forEach items="${deliveryServices}" var="deliveryService">
                            <c:set var="ds" value="${deliveryService}" scope="request"/>
                            <c:import url="/fragment/shipping/type/${deliveryService.code}Service.jsp"/>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>