<%--@elvariable id="store" type="com.coretex.shop.model.shop.ReadableMerchantStore"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page import="com.coretex.core.business.constants.Constants" %>

<%@ page session="false" %>


<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>


<script>


    $(document).ready(function () {

        if ($("#code").val() == "") {
            $('.btn').addClass('disabled');
        }


        <c:choose>
        <c:when test="${store.address.stateProvince!=null && store.address.stateProvince!=''}">
        $('.zone-list').hide();
        $('#storestateprovince').show();
        $('#storestateprovince').val('<c:out value="${store.address.stateProvince}"/>');
        getZones('<c:out value="${store.address.country}" />');
        </c:when>
        <c:otherwise>
        $('.zone-list').show();
        $('#storestateprovince').hide();
        getZones('<c:out value="${store.address.country}" />');
        </c:otherwise>
        </c:choose>

        $(".country-list").change(function () {
            getZones($(this).val());
        })


    });

    $.fn.addItems = function (data) {
        $(".zone-list > option").remove();
        return this.each(function () {
            var list = this;
            $.each(data, function (index, itemData) {
                var option = new Option(itemData.name, itemData.code);
                list.add(option);
            });
        });
    };

    function getZones(countryCode) {

        var url = '<c:url value="/admin/reference/provinces.html"/>?lang=<c:out value="${requestScope.LANGUAGE.iso}"/>';

        $.ajax({
            type: 'POST',
            url: url,
            data: 'countryCode=' + countryCode,
            dataType: 'json',
            success: function (response) {

                var status = isc.XMLTools.selectObjects(response, "/response/status");
                if (status == 0 || status == 9999) {

                    var data = isc.XMLTools.selectObjects(response, "/response/data");
                    if (data && data.length > 0) {

                        $('.zone-list').show();
                        $('#storestateprovince').hide();
                        $(".zone-list").addItems(data);
                        <c:if test="${store.address!=null}">
                        $('.zone-list').val('<c:out value="${store.address.stateProvince}"/>');
                        $('#storestateprovince').val('');
                        </c:if>
                    } else {
                        $('.zone-list').hide();
                        $('#storestateprovince').show();
                        <c:if test="${store.address.stateProvince!=null}">
                        $('#storestateprovince').val('<c:out value="${store.address.stateProvince}"/>');
                        </c:if>
                    }
                } else {
                    $('.zone-list').hide();
                    $('#storestateprovince').show();
                }


            },
            error: function (xhr, textStatus, errorThrown) {
                alert('error ' + errorThrown);
            }

        });
    }


    function validateCode() {
        $('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
        $('#checkCodeStatus').show();
        var storeCode = $("#code").val();
        var id = $("#id").val();
        checkCode(storeCode, id, '<c:url value="/admin/store/checkStoreCode.html" />');
    }

    function callBackCheckCode(msg, code) {

        if (code == 0) {
            $('.btn').removeClass('disabled');
        }
        if (code == 9999) {

            $('#checkCodeStatus').html('<font color="green"><s:message code="message.code.available" text="This code is available"/></font>');
            $('#checkCodeStatus').show();
            $('.btn').removeClass('disabled');
        }
        if (code == 9998) {

            $('#checkCodeStatus').html('<font color="red"><s:message code="message.code.exist" text="This code already exist"/></font>');
            $('#checkCodeStatus').show();
            $('.btn').addClass('disabled');
        }

    }


</script>


<div class="tabbable">


    <jsp:include page="/common/adminTabs.jsp"/>

    <h3><s:message code="label.store.title" text="Merchant store"/></h3>
    <br/>


    <c:url var="merchant" value="/admin/store/save.html"/>


    <form:form method="POST" modelAttribute="store" action="${merchant}">

        <form:errors path="*" cssClass="alert alert-error" element="div"/>
        <div id="store.success" class="alert alert-success" style="
        <c:choose>
        <c:when test="${success!=null}">display:block;</c:when>
            <c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success"
                                                                            text="Request successfull"/></div>

        <div class="control-group">
            <label><s:message code="label.storename" text="Name"/></label>
            <div class="controls">
                <form:input cssClass="input-large highlight" path="name"/>
                <span class="help-inline"><form:errors path="name" cssClass="error"/></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.storecode" text="Store code"/></label>
            <div class="controls">
                <c:choose>
                    <c:when test="${store.code=='DEFAULT'}">
                        <span class="input-large uneditable-input">${store.code}</span><form:hidden path="code"/>
                    </c:when>
                    <c:otherwise>
                        <form:input cssClass="input-large highlight" path="code" onblur="validateCode()"/>
                    </c:otherwise>
                </c:choose>
                <span class="help-inline"><div id="checkCodeStatus" style="display:none;"></div><form:errors path="code"
                                                                                                             cssClass="error"/></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.storephone" text="Phone"/></label>
            <div class="controls">
                <form:input cssClass="input-large highlight" path="phone"/>
                <span class="help-inline"><form:errors path="phone" cssClass="error"/></span>
            </div>

        </div>

        <div class="control-group">
            <label><s:message code="label.storeemailaddress" text="Email"/></label>
            <div class="controls">
                <form:input cssClass="input-large highlight" path="email"/>
                <span class="help-inline"><form:errors path="email" cssClass="error"/></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.storeaddress" text="Address"/></label>
            <div class="controls">
                <form:input cssClass="input-large" path="address.address"/>
                <span class="help-inline"><form:errors path="address.address" cssClass="error"/></span>
            </div>
        </div>


        <div class="control-group">
            <label><s:message code="label.storecity" text="City"/></label>
            <div class="controls">
                <form:input cssClass="input-large highlight" path="address.city"/>
                <span class="help-inline"><form:errors path="address.city" cssClass="error"/></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.storecountry" text="Store Country"/></label>
            <div class="controls">

                <form:select cssClass="country-list highlight" path="address.country">
                    <form:options items="${countries}" itemValue="isoCode" itemLabel="name"/>
                </form:select>
                <span class="help-inline"><form:errors path="address.country" cssClass="error"/></span>
            </div>
        </div>


        <div class="control-group">
            <label><s:message code="label.storezone" text="Store state / province"/></label>
            <div class="controls">
                <form:select cssClass="zone-list highlight" path="address.stateProvince"/>
                <input type="text" class="input-large highlight" id="storestateprovince" name="stateProvince"
                       value="${store.address.stateProvince}"/>
                <span class="help-inline"><form:errors path="address.stateProvince" cssClass="error"/></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.storepostalcode" text="Postal code"/></label>
            <div class="controls">
                <form:input cssClass="input-large highlight" path="address.postalCode"/>
                <span class="help-inline"><form:errors path="address.postalCode" cssClass="error"/></span>
            </div>
        </div>


        <div class="control-group">
            <label><s:message code="label.supportedlanguages" text="Supported languages"/></label>
            <div class="controls">

                <form:checkboxes cssClass="highlight" items="${languages}" itemValue="code" itemLabel="code"
                                 path="supportedLanguages"/>
                <span class="help-inline"><form:errors path="supportedLanguages" cssClass="error"/></span>
            </div>
        </div>


        <div class="control-group">
            <label><s:message code="label.defaultlanguage" text="Default language"/></label>
            <div class="controls">

                <form:select items="${languages}" itemValue="code" itemLabel="code" path="defaultLanguage"/>
                <span class="help-inline"></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.currency" text="Currency"/></label>
            <div class="controls">
                <form:select items="${currencies}" itemValue="code" itemLabel="code" path="currency"/>
                <span class="help-inline"></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.store.currency.format" text="National currency format"/></label>
            <div class="controls">

                <form:checkbox path="currencyFormatNational" />
                <span class="help-inline"><s:message code="label.store.currency.format.help"
                                                     text="National currency format ex $1,345.79 or International currency format ex USD1,345.79"/></span>
            </div>
        </div>


        <div class="control-group">
            <label><s:message code="label.store.weightunit" text="Weight units"/></label>
            <div class="controls">

                <form:select items="${weights}" path="weight" itemValue="code" itemLabel="name"/>
                <span class="help-inline"></span>
            </div>
        </div>


        <div class="control-group">
            <label><s:message code="label.store.sizeunit" text="Size units"/></label>
            <div class="controls">

                <form:select items="${sizes}" path="dimension" itemValue="code" itemLabel="name"/>
                <span class="help-inline"></span>
            </div>
        </div>


        <div class="control-group">
            <label><s:message code="label.store.inbusinesssince" text="Web site operating since"/></label>
            <div class="controls">
                <input id="dateBusinessSince" name="dateBusinessSince" value="${store.inBusinessSince}" class="small"
                       type="text"
                       data-date-format="${Constants.DEFAULT_DATE_FORMAT}"
                       data-datepicker="datepicker">
                <span class="help-inline"><form:errors path="inBusinessSince" cssClass="error"/></span>
            </div>
        </div>

        <div class="control-group">
            <label><s:message code="label.store.useCache" text="Use cache"/></label>
            <div class="controls">

                <form:checkbox path="useCache"/>
                <span class="help-inline"></span>
            </div>
        </div>

<%--        <div class="control-group">--%>
<%--            <label><s:message code="label.store.baseurl" text="Store base url"/></label>--%>
<%--            <div class="controls">--%>
<%--	                                    <span class="add-on">--%>
<%--											http://--%>
<%--										</span>--%>
<%--                <form:input cssClass="input-medium highlight" path="domainName"/>--%>
<%--                <span class="help-inline"><form:errors path="domainName" cssClass="error"/></span>--%>
<%--            </div>--%>
<%--        </div>--%>

        <form:hidden path="id"/>
        <form:hidden path="logo.path"/>
        <form:hidden path="template"/>

        <div class="form-actions">
            <div class="pull-right">
                <button type="submit" class="btn btn-success"><s:message code="button.label.submit2"
                                                                         text="Submit"/></button>
            </div>
        </div>


    </form:form>


</div>