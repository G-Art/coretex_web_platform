<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib prefix="tags-checkout" tagdir="/WEB-INF/tags/checkout" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>


<c:set var="creditCardInformationsPage" value="creditCardInformations" scope="request"/>

<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>


<script type="text/html" id="shipToPostOffice">
    <div class="checkout-box">
                        <span class="box-title">
                            <p class="p-title"><s:message code="label.customer.shippinginformation"
                                                          text="Shipping information"/></p>
                        </span>
        <div class="row-fluid common-row row">
            <div class="span4 col-md-4 postOfficeSelector">
                <div class="form-group">
                    <label for="postOfficeInput"><s:message code="label.generic.postOffice" text="Post office"/></label>
                    <div class="controls">
                        <select name="postOffice" id="postOfficeInput" class="form-control form-control-sm rounded-lg">
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/html" id="shipToHome">
    <div class="checkout-box">
                        <span class="box-title">
                            <p class="p-title"><s:message code="label.customer.shippinginformation"
                                                          text="Shipping information"/></p>
                        </span>
    </div>
</script>


<!-- subtotals template -->
<script type="text/html" id="subTotalsTemplate">
    {{#subTotals}}
    <tr class="subt">
        <td colspan="2">{{title}}</td>
        <td><strong>{{total}}</strong>
        </td>
    </tr>
    {{/subTotals}}
</script>

<!-- total template -->
<script type="text/html" id="totalTemplate">
    <span class="total-box-grand-total">
			<span class="total-box-label">
			  <s:message code="order.total.total" text="Total"/>
			  &nbsp;<span class="total-box-price grand-total">{{grandTotal}}</span>
			</span>
		</span>
</script>

<!-- shipping template -->
<script type="text/html" id="shippingTemplate">

    <label class="control-label">
        <s:message code="label.shipping.options" text="Shipping options"/>
        {{#showHandling}}
        &nbsp;(<s:message code="label.shipping.handlingfees" text="Handling fees"/>&nbsp;{{handlingText}})
        {{/showHandling}}
    </label>
    <div id="shippingOptions" class="controls">
        {{#shippingOptions}}
        <label class="radio">
            <input type="radio" name="selectedShippingOption.optionId" code="{{shippingModuleCode}}"
                   class="shippingOption" id="{{optionId}}" value="{{optionId}}" {{#checked}} checked="checked" {{/checked}}>
            {{description}} - {{optionPriceText}}
            <br/>
            {{#note}}
            <small>{{note}}</small>
            {{/note}}
        </label>
        {{/shippingOptions}}
    </div>

</script>


<script>

    $(document).ready(function () {

        if ($("input[name='deliveryMethod']:checked").val()) {
            showDeliverySpecificData($("input[name='deliveryMethod']:checked")[0]);
        }

        var citiesProperties = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.whitespace,
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '<c:url value="/shop/order/dservice/novaposhta/city"/>?q=%QUERY',
                wildcard: '%QUERY',
                filter: function (parsedResponse) {
                    // parsedResponse is the array returned from your backend
                    //console.log(parsedResponse);

                    return parsedResponse;
                }
            }
        });
        citiesProperties.initialize();

        var searchTemplate = Hogan.compile(
            '<div class="tt-suggestion tt-selectable">' +
            '<span style="color: black; ">{{SettlementTypeDescription}} {{Description}} {{AreaDescription}}</span>' +
            '</div>');

        $('#cityInput').typeahead({
            hint: true,
            highlight: true,
            minLength: 1
        }, {
            name: 'citiesProperties',
            source: citiesProperties.ttAdapter(),
            display: 'Description',
            templates: {
                suggestion: function (data) {
                    return searchTemplate.render(data);
                }
            }
        }).on('typeahead:selected', function (ev, datum) {
                let cityRef = datum["Ref"];
                $('#cityInputCode').val(cityRef);
                if ($("input[name='deliveryMethod']:checked").val()) {
                    showDeliverySpecificData($("input[name='deliveryMethod']:checked")[0]);
                }
            }
        );

        bindActions()
    });

    function showDeliverySpecificData(method) {
        let cityRef = $('#cityInputCode').val();
        let methodTemplate = Hogan.compile(document.getElementById(method.value).innerHTML);
        $('#shippingMethodSection').html(methodTemplate.render());
        if (cityRef !== ""){
        calculateTotal();
            $.getJSON('<c:url value="/shop/order/dservice/novaposhta/office"/>?ref=' + cityRef, {}, function (json) {
                let postOfficeInput = $("#postOfficeInput");
                postOfficeInput.children().remove();
                $.each(json, function (item) {
                    postOfficeInput.append(new Option(this.Description, this.Ref));
                });
            });
        }
    }


    <!-- creates a json representation of the form -->
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    <!-- checkout form id -->
    var checkoutFormId = '#checkoutForm';

    <!-- checkout field id -->
    var formErrorMessageId = '#formErrorMessage';
    var useDistanceWindow = false;

    function showErrorMessage(message) {


        showResponseErrorMessage(message);
        $('#submitOrder').addClass('btn-disabled');
        $('#submitOrder').prop('disabled', true);

        $(formErrorMessageId).addClass('alert-error alert-danger');
        $(formErrorMessageId).removeClass('alert-success');
        $(formErrorMessageId).html('<!--<img src="<c:url value="/resources/img/icon_error.png"/>" width="40"/>&nbsp;--><strong><font color="red">' + message + '</font></strong>');
        $(formErrorMessageId).show();

    }

    function showResponseErrorMessage(message) {

        $('#checkoutError').addClass('alert');
        $('#checkoutError').addClass('alert-error alert-danger');
        $('#checkoutError').html(message);

    }

    function resetErrorMessage() {

        $('#checkoutError').html('');
        $('#checkoutError').removeClass('alert');
        $('#checkoutError').removeClass('alert-error alert-danger');
        $('.error').html('');

    }


    function bindActions() {


        $("#submitOrder").click(function (e) {
            e.preventDefault();//do not submit form
            resetErrorMessage();

            $('#pageContainer').showLoading();

            submitForm();
        });
    }

    function submitForm() {
        log('Checkout ');
        $('#pageContainer').hideLoading();
        $('#checkoutForm').submit();
    }

    //when changing a shipping method
    function calculateTotal() {
        resetErrorMessage();
        $('#pageContainer').showLoading();
        var data = $(checkoutFormId).serialize();
        console.log(data);
        formValid = false;

        $.ajax({
            type: 'POST',
            url: '<c:url value="/shop/order/calculateOrderTotal.json"/>',
            data: data,
            cache: false,
            dataType: 'json',
            success: function (response) {

                $('#pageContainer').hideLoading();
                if (response.errorMessage !== null && response.errorMessage !== '') {
                    showErrorMessage(response.errorMessage);
                    return;
                }

                //console.log(response);

                $('#summary-table tr.subt').remove();
                $('#totalRow').html('');
                var subTotalsTemplate = Hogan.compile(document.getElementById("subTotalsTemplate").innerHTML);
                var totalTemplate = Hogan.compile(document.getElementById("totalTemplate").innerHTML);
                var subTotalsRendered = subTotalsTemplate.render(response);
                var totalRendred = totalTemplate.render(response);


                //console.log(rendered);
                $('#summaryRows').append(subTotalsRendered);
                $('#totalRow').html(totalRendred);
            },
            error: function (xhr, textStatus, errorThrown) {
                $('#pageContainer').hideLoading();
                console.log('error ' + errorThrown);
            }

        });
    }

</script>

<div id="main-content" class="container row-fluid">
    <h1 class="checkout-title"><s:message code="label.checkout" text="Checkout"/></h1>
    <sec:authorize access="!hasRole('AUTH_CUSTOMER') and !fullyAuthenticated">
        <p class="muted common-row"><s:message code="label.checkout.logon"
                                               text="Logon or signup to simplify the online purchase process!"/></p>
    </sec:authorize>

    <div class="row-fluid common-row" id="checkout">
        <div class="span12 col-md-12 no-padding">

            <!-- If error messages -->
            <div id="checkoutError"
                 class="<c:if test="${errorMessages!=null}">alert  alert-error alert-danger </c:if>">
                <c:if test="${errorMessages!=null}">
                    <c:out value="${errorMessages}"/>
                </c:if>
            </div>
            <!--alert-error-->
            <!-- row fluid span -->
            <div class="row">
                <!-- left column -->
                <div class="span8 col-md-8 no-padding-left">
                    <c:set var="commitUrl" value="${pageContext.request.contextPath}/shop/order/commitOrder.html"/>
                    <form class="m-0" id="checkoutForm" action="${commitUrl}" method="post">
                        <div id="shippingVariantBox" class="checkout-box">
                        <span class="box-title">
												<p class="p-title"><s:message code="label.customer.shippinginformation"
                                                                              text="Shipping information"/></p>
											</span>
                            <div class="row-fluid common-row row">
                                <div class="span4 col-md-4">
                                    <div class="form-group">
                                        <label for="userNameInput"><s:message code="label.generic.name"
                                                                           text="Name"/></label>
                                        <div class="controls">
                                            <input name="userName" id="userNameInput" value="${cart.userName}"
                                                   class="form-control form-control-sm rounded-lg" type="text"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="span4 col-md-4">
                                    <div class="form-group">
                                        <label for="cityInput"><s:message code="label.generic.city"
                                                                          text="City"/></label>
                                        <div class="controls">
                                            <input name="city" id="cityInput" value="${cart.cityName}" autocomplete="off"
                                                   class="form-control form-control-sm rounded-lg" type="text"/>
                                            <input name="cityCode" id="cityInputCode" type="hidden" value="${cart.cityCode}"/>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="row-fluid common-row row">
                                <div class="span4 col-md-4">
                                    <div class="form-group">
                                        <label for="emailInput"><s:message code="label.generic.email"
                                                                          text="Email"/></label>
                                        <div class="controls">
                                            <input name="email" id="emailInput" value="${cart.email}"
                                                   class="form-control form-control-sm rounded-lg" type="text"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="span4 col-md-4">
                                    <div class="form-group">
                                        <label for="phoneInput"><s:message code="label.generic.phone"
                                                                           text="Phone number"/></label>
                                        <div class="controls">
                                            <input name="phone" id="phoneInput" value="${cart.phone}"
                                                   class="form-control form-control-sm rounded-lg" type="text"/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <tags-checkout:checkoutShippingMethods/>
                        <%--                    <tags-checkout:checkoutAddressForm/> TODO: depend on selected shipping method--%>
                        <div id="shippingMethodSection">

                        </div>
                    </form>

                </div>
                <!-- end left column -->

                <!-- Order summary right column -->
                <div class="span4 col-md-4 no-padding">
                    <tags-checkout:checkoutSummaryBox/>

                    <c:if test="${requestScope.CONFIGS['displayCustomerAgreement']==true}">
                        <!-- customer agreement -->
                        <div class="checkout-box" id="customerAgreementSection" class="">
                            <label id="customerAgreement" class="checkbox">
                                <s:message code="NotEmpty.customer.agreement"
                                           text="Please make sure you agree with terms and conditions"
                                           var="msgAgreement"/>
                                <form:checkbox path="customerAgreed" id="customerAgreed" cssClass="required"
                                               title="${msgAgreement}"/>
                                <a href="javascript:return false;" id="clickAgreement"><s:message
                                        code="label.customer.order.agreement"
                                        text="I agree with the terms and conditions"/></a>
                            </label>
                            <div id="customer-agreement-area">
                                <c:choose>
                                    <c:when test="${requestScope.CONTENT['agreement']!=null}">
                                        <sm:pageContent contentCode="agreement"/>
                                    </c:when>
                                    <c:otherwise>
                                        <s:message code="message.content.missing.agreement"
                                                   text="Content with code 'agreement' does not exist"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:if>

                    <div id="formErrorMessage" class="alert">
                    </div>
                    <!-- Submit -->
                    <div class="form-actions">
                        <div class="pull-right">
                            <button id="submitOrder" type="button" class="btn btn-large btn-success
												<c:if test="${errorMessages!=null}"> btn-disabled</c:if>"
                                    <c:if test="${errorMessages!=null}"> disabled="true"</c:if>
                            ><s:message code="button.label.submitorder" text="Submit order"/></button>

                            <!-- submit can be a post or a pre ajax query -->
                        </div>
                    </div>

                </div>
                <!-- end right column -->

            </div>
            <!-- end row fluid span -->
        </div>
        <!-- end span 12 -->

    </div>
    <!-- end row fluid -->

</div>