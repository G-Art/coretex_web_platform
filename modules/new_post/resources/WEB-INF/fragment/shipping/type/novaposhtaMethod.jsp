<%--@elvariable id="deliveryService" type="com.coretex.newpost.data.NewPostDeliveryServiceData"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<h3>
<s:message
        code="module.shipping.${deliveryService.code}"
        arguments="${requestScope.ADMIN_STORE.storeName}"
        text="${deliveryService.name.get(requestScope.LOCALE.toString())}"/></h3>
<br/>


<s:message code="module.shipping.${deliveryService.code}.note" text=""/><br/>

<c:url var="saveShippingMethod" value="/admin/shipping/method/save"/>


<form:form method="POST" modelAttribute="deliveryService" action="${saveShippingMethod}" >


    <form:errors path="*" cssClass="alert alert-error" element="div"/>
    <div id="store.success" class="alert alert-success" style="
    <c:choose>
    <c:when test="${success!=null}">display:block;</c:when>
    <c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success"
                                                                    text="Request successfull"/></div>


    <div class="control-group">
        <label><s:message code="label.entity.enabled" text="Active"/></label>
        <div class="controls">
            <form:checkbox path="active"/>
        </div>
    </div>

    <div class="control-group">
        <label><s:message code="label.entity.code" text="Code"/></label>
        <div class="controls">
            <form:input path="code" disabled="true"/>
        </div>
    </div>

    <tag:multiLanguageInput path="name" map="${deliveryService.name}" message="label.entity.name" messageText="Name"/>

    <div class="control-group">
        <label><s:message code="label.entity.apikey" text="Api key"/></label>
        <div class="controls">
            <form:input path="apiKey"/>
        </div>
    </div>
    <div class="control-group">
        <label><s:message code="label.entity.endpoint" text="Endpoint"/></label>
        <div class="controls">
            <form:input path="endpoint"/>
        </div>
    </div>
    <div class="control-group">
        <label><s:message code="label.entity.image" text="Image"/></label>
        <div class="controls">
            <form:input path="image" disabled="true"/>
        </div>
    </div>
    <form:hidden path="uuid"/>

    <div class="form-actions">
        <div class="pull-right">
            <button type="submit" class="btn btn-success"><s:message code="button.label.submit"
                                                                     text="Submit"/></button>
        </div>
    </div>


</form:form>

<c:forEach var="deliveryType" items="${deliveryService.deliveryTypes}">
    <div class="accordion-group">
        <div class="accordion-heading">
            <a class="accordion-toggle" data-toggle="collapse"
               data-parent="#accordion_${deliveryService.code}"
               href="#collapse_${deliveryType.uuid}">

                    ${deliveryType.name[requestScope.LOCALE.toString()]}
            </a>
        </div>
        <div id="collapse_${deliveryType.uuid}" class="accordion-body collapse">
            <div class="accordion-inner">
                <table class="table table-striped table-condensed">
                    <tr>
                        <td> Code</td>
                        <td> ${deliveryType.code} </td>
                    </tr>
                    <tr>
                        <td> Name</td>
                        <td>
                            <tag:multiLanguageDisplaying  map="${deliveryType.name}" message="label.entity.name" messageText="Name"/>
                        </td>
                    </tr>
                    <tr>
                        <td> Active</td>
                        <td>
                            <c:choose>
                                <c:when test="${deliveryType!=null && deliveryType.active == true}">
                                    <img src="<c:url value="/resources/img/icon_green_on.gif"/>"
                                         width="12">&nbsp;
                                </c:when>
                                <c:otherwise>
                                    <img src="<c:url value="/resources/img/icon_red_on.gif"/>"
                                         width="12">&nbsp;
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td> Pay on delivery</td>
                        <td><c:choose>
                            <c:when test="${deliveryType!=null && deliveryType.payOnDelivery == true}">
                                <img src="<c:url value="/resources/img/icon_green_on.gif"/>"
                                     width="12">&nbsp;
                            </c:when>
                            <c:otherwise>
                                <img src="<c:url value="/resources/img/icon_red_on.gif"/>"
                                     width="12">&nbsp;
                            </c:otherwise>
                        </c:choose>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</c:forEach>

