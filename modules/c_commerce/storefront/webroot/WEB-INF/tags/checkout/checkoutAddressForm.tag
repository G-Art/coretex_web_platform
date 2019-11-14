<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>


<c:set var="commitUrl" value="${pageContext.request.contextPath}/shop/order/commitOrder.html"/>
<form:form id="checkoutForm" method="POST" enctype="multipart/form-data" modelAttribute="order"
           action="${commitUrl}">
    <!-- Billing box -->
    <div id="shippingBox" class="checkout-box">
											<span class="box-title">
												<p class="p-title"><s:message code="label.customer.shippingaddress"
                                                                              text="Shipping information"/></p>
											</span>


        <!-- First name - Last name -->
        <div class="row-fluid common-row row">
            <div class="span4 col-md-4">

                <div class="control-group form-group">
                    <label><s:message code="label.generic.firstname" text="First Name"/></label>
                    <div class="controls">
                        <s:message code="NotEmpty.customer.firstName" text="First name is required"
                                   var="msgFirstName"/>
                        <form:input id="customer.firstName"
                                    cssClass="input-large required form-control-sm"
                                    path="customer.billing.firstName" autofocus="autofocus"
                                    title="${msgFirstName}"/>
                        <form:errors path="customer.billing.firstName" cssClass="error"/>
                        <span id="error-customer.billing.firstName" class="error"></span>
                    </div>
                </div>
            </div>
            <div class="span4 col-md-4">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.lastname" text="Last Name"/></label>
                    <div class="controls">
                        <s:message code="NotEmpty.customer.lastName" text="Last name is required"
                                   var="msgLastName"/>
                        <form:input id="customer.lastName"
                                    cssClass="input-large required form-control-sm"
                                    maxlength="32" path="customer.billing.lastName"
                                    title="${msgLastName}"/>
                        <form:errors path="customer.billing.lastName" cssClass="error"/>
                        <span id="error-customer.billing.lastName" class="error"></span>
                    </div>
                </div>
            </div>
        </div>


        <!-- email company -->
        <div class="row-fluid common-row row">
            <div class="span4 col-md-4">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.email" text="Email address"/></label>
                    <div class="controls">
                        <s:message code="NotEmpty.customer.emailAddress"
                                   text="Email address is required" var="msgEmail"/>
                        <form:input id="customer.emailAddress"
                                    cssClass="input-large required email form-control-sm"
                                    path="customer.emailAddress" title="${msgEmail}"/>
                        <form:errors path="customer.emailAddress" cssClass="error"/>
                        <span id="error-customer.emailAddress" class="error"></span>
                    </div>
                </div>
            </div>
            <div class="span4 col-md-4">
                <div class="control-group form-group">
                    <label><s:message code="label.customer.billing.company"
                                      text="Billing company"/></label>
                    <div class="controls">
                        <form:input id="customer.billing.company"
                                    cssClass="input-large form-control-sm"
                                    path="customer.billing.company"/>
                        <form:errors path="customer.billing.company" cssClass="error"/>
                        <span id="error-customer.billing.company" class="error"></span>
                    </div>
                </div>
            </div>
        </div>

        <!--  streetProperties address -->
        <div class="row-fluid common-row row">
            <div class="span8 col-md-8">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.streetaddress"
                                      text="Street address"/></label>
                    <div class="controls">
                        <s:message code="NotEmpty.customer.billing.address"
                                   text="Address is required" var="msgAddress"/>
                        <form:input id="customer.billing.address"
                                    cssClass="input-xxlarge required form-control-sm"
                                    path="customer.billing.address" title="${msgAddress}"/>
                        <form:errors path="customer.billing.address" cssClass="error"/>
                        <span id="error-customer.billing.address" class="error"></span>
                    </div>
                </div>
            </div>
        </div>

        <!-- city - postal code -->
        <div class="row-fluid common-row row">
            <div class="span4 col-md-4">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.city" text="City"/></label>
                    <div class="controls">
                        <s:message code="NotEmpty.customer.billing.city" text="City is required"
                                   var="msgCity"/>
                        <form:input id="customer.billing.city"
                                    cssClass="input-large required form-control-sm"
                                    path="customer.billing.city" title="${msgCity}"/>
                        <form:errors path="customer.billing.city" cssClass="error"/>
                        <span id="error-customer.billing.city" class="error"></span>
                    </div>
                </div>
            </div>
            <div class="span4 col-md-4">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.postalcode" text="Postal code"/></label>
                    <div class="controls">
                        <s:message code="NotEmpty.customer.billing.postalCode"
                                   text="Postal code is required" var="msgPostalCode"/>
                        <form:input id="billingPostalCode"
                                    cssClass="input-large required billing-postalCode form-control-sm"
                                    path="customer.billing.postalCode" title="${msgPostalCode}"/>
                        <form:errors path="customer.billing.postalCode" cssClass="error"/>
                        <span id="error-customer.billing.postalCode" class="error"></span>
                    </div>
                </div>
            </div>
        </div>

        <!-- state province -->
        <div class="row-fluid common-row row">
            <div class="span8 col-md-8">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.stateprovince"
                                      text="State / Province"/></label>
                    <div class="controls">
                        <form:select cssClass="zone-list form-control-sm"
                                     id="billingStateList" path="customer.billing.zone"/>
                        <s:message code="NotEmpty.customer.billing.stateProvince"
                                   text="State / Province is required" var="msgStateProvince"/>
                        <form:input class="input-large required form-control-sm"
                                    id="billingStateProvince" maxlength="100"
                                    name="billingStateProvince"
                                    path="customer.billing.stateProvince"
                                    title="${msgStateProvince}"/>
                        <form:errors path="customer.billing.stateProvince" cssClass="error"/>
                        <span id="error-customer.billing.stateProvince" class="error"></span>
                    </div>
                </div>
            </div>
        </div>

        <!-- country - phone - ship checkbox -->
        <div class="row-fluid common-row row">
            <div class="span4 col-md-4">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.country" text="Country"/></label>
                    <div class="controls">
                        <form:select cssClass="billing-country-list form-control-sm"
                                     path="customer.billing.country">
                            <form:options items="${countries}" itemValue="isoCode"
                                          itemLabel="name"/>
                        </form:select>
                    </div>
                </div>
            </div>

            <div class="span4 col-md-4">
                <div class="control-group form-group">
                    <label><s:message code="label.generic.phone" text="Phone number"/></label>
                    <div class="controls ">
                        <s:message code="NotEmpty.customer.billing.phone"
                                   text="Phone number is required" var="msgPhone"/>
                        <form:input id="customer.billing.phone"
                                    cssClass="input-large required billing-phone form-control-sm"
                                    path="customer.billing.phone" title="${msgPhone}"/>
                        <form:errors path="customer.billing.phone" cssClass="error"/>
                        <span id="error-customer.billing.phone" class="error"></span>
                    </div>
                </div>
            </div>

        </div>

        <c:if test="${shippingQuote!=null}">
            <!-- display only if a shipping quote exist -->
            <div class="row-fluid common-row row">
                <div class="span8 col-md-8">
                    <label id="useAddress" class="checkbox">
                        <form:checkbox path="shipToBillingAdress" id="shipToBillingAdress"/>
                        <s:message code="label.customer.shipping.shipaddress"
                                   text="Ship to this address"/></label>
                </div>
            </div>
        </c:if>
    </div>
</form:form>