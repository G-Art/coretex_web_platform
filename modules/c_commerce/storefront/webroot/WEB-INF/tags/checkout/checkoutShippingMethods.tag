<%--@elvariable id="deliveryServices" type="java.util.List<com.coretex.newpost.data.NewPostDeliveryServiceData>"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<div id="shippingVariantBox" class="checkout-box">
                        <span class="box-title">
												<p class="p-title"><s:message code="label.shipping.methods"
                                                                              text="Shipping methods"/></p>
											</span>

    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <c:forEach var="deliveryService" items="${deliveryServices}" varStatus="status">

            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="heading${status.index}">
                    <h4 class="panel-title">
                        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${status.index}"
                           aria-expanded="${status.index == 0 ? 'true' : 'false'}" aria-controls="collapse${status.index}">
                            <div class="row">
                                <div class="col-md-4 align-self-center">
                                    <span class="align-middle">${deliveryService.name[requestScope.LANGUAGE.iso]}</span>
                                </div>
                                <div class="offset-md-6 col-md-2">
                                    <img class="media-object"
                                         src="<c:url value="/resources/${deliveryService.image}"/>" height="40px" />
                                </div>
                            </div>


                        </a>
                    </h4>
                </div>
                <div id="collapse${status.index}" class="panel-collapse collapse in ${status.index == 0 ? 'show' : ''}" role="tabpanel"
                     aria-labelledby="heading${status.index}">
                    <div class="panel-body">

                        <c:forEach var="deliveryMethod" items="${deliveryService.deliveryTypes}" varStatus="s">
                            <div class="radio row text-justify">
                                <div class="col-md-10 d-flex align-items-center">
                                    <label class="text m-0 ml-2">
                                        <input onchange="showDeliverySpecificData(this)" type="radio" name="deliveryMethod" id="optionsRadios${s.index}" ${deliveryMethod.code eq cart.deliveryMethod ? 'checked' : ''}
                                               value="${deliveryMethod.code}">
                                            ${deliveryMethod.name[requestScope.LANGUAGE.iso]}
                                    </label>
                                </div>

                                <div class="col-md-2 ">
                                    <c:if test="${deliveryMethod.payOnDelivery}">
                                        <div class="media-right media-middle">
                                            <img class="media-object" alt="COD"
                                                 src="<c:url value="/resources/img/cod.svg"/>" width="35" height="35"/>
                                        </div>

                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>