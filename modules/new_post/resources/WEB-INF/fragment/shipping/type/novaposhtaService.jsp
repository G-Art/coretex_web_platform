<%--@elvariable id="deliveryService" type="com.coretex.newpost.data.NewPostDeliveryServiceData"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<c:set var="deliveryService" value="${requestScope.ds}"/>

<tr>
    <td>
        <c:choose>
            <c:when test="${deliveryService!=null && deliveryService.active == true}">
                <img src="<c:url value="/resources/img/icon_green_on.gif"/>" width="12">&nbsp;
            </c:when>
            <c:otherwise>
                <img src="<c:url value="/resources/img/icon_red_on.gif"/>" width="12">&nbsp;
            </c:otherwise>
        </c:choose>
    </td>
    <td>
        <label>
            <a href="<c:url value="/admin/shipping/method/${deliveryService.code}"/>">
                <s:message code="module.shipping.${deliveryService.code}"
                           arguments="${requestScope.ADMIN_STORE.storeName}"
                           text="${deliveryService.name[requestScope.LOCALE.toString()]}"/></a>
        </label>
        <c:if test="${deliveryService.deliveryTypes!=null}">
            <div class="accordion" id="accordion_${deliveryService.code}">
                <c:forEach items="${deliveryService.deliveryTypes}" var="deliveryType">
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

            </div>

        </c:if>


    </td>
    <td>
        <c:if test="${deliveryService.image!=null}">
            <img style="height: 35px;" src="<c:url value="/resources/${deliveryService.image}" />">
        </c:if>
    </td>
</tr>