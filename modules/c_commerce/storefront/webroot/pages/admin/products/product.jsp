<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib prefix="lang" tagdir="/WEB-INF/tags/lang" %>
<%@ page import="com.coretex.core.business.constants.Constants" %>
<%--@elvariable id="product" type="com.coretex.shop.admin.forms.ProductForm"--%>

<%@ page session="false" %>
<script type="text/javascript">
    var priceFormatMessage = '<s:message code="message.price.cents" text="Wrong format" />';
</script>

<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>


<script type="text/javascript">


    $(function () {
        $('#sku').alphanumeric();
        $('#productPriceAmount').numeric({allow: "."});
        $('#quantity').numeric();
        $('#ordermin').numeric();
        $('#ordermax').numeric();
        $('#order').numeric();
        $('#weight').numeric({allow: "."});
        $('#width').numeric({allow: "."});
        $('#length').numeric({allow: "."});
        $('#hight').numeric({allow: "."});
        <%--        <c:forEach items="${product.descriptions}" var="description" varStatus="counter">--%>
        <%--        $("#name${counter.index}").friendurl({id: 'seUrl${counter.index}'});--%>
        <%--        </c:forEach>--%>
    });


    function removeImage(imageId) {
        $("#store.error").show();
        $.ajax({
            type: 'POST',
            url: '<c:url value="/admin/products/product/removeImage.html"/>',
            data: 'imageId=' + imageId,
            dataType: 'json',
            success: function (response) {

                var status = isc.XMLTools.selectObjects(response, "/response/status");
                if (status == 0 || status == 9999) {

                    //remove delete
                    $("#imageControlRemove").html('');
                    //add field
                    $("#imageControl").html('<input class=\"input-file\" id=\"image\" name=\"image\" type=\"file\">');
                    $(".alert-success").show();

                } else {

                    //display message
                    $(".alert-error").show();
                }


            },
            error: function (xhr, textStatus, errorThrown) {
                alert('error ' + errorThrown);
            }

        });
    }


</script>


<div class="tabbable">


    <jsp:include page="/common/adminTabs.jsp"/>

    <div class="tab-content">

        <div class="tab-pane active" id="catalogue-section">


            <div class="sm-ui-component">


                <c:if test="${product.uuid!=null}">
                    <c:set value="${product.uuid}" var="productId" scope="request"/>
                    <jsp:include page="/pages/admin/products/product-menu.jsp"/>
                </c:if>


                <h3>
                    <c:choose>
                        <c:when test="${product.uuid!=null}">
                            <s:message code="label.product.edit" text="Edit product"/> <c:out
                                value="${product.sku}"/>
                        </c:when>
                        <c:otherwise>
                            <s:message code="label.product.create" text="Create product"/>
                        </c:otherwise>
                    </c:choose>

                </h3>
                <br/>
                <c:if test="${product.uuid != null}">
                    <strong><sm:productUrl product="${productItem}"/></strong><br/>
                </c:if>
                <br/><br/>

                <!-- Nav tabs -->
                <ul class="nav nav-tabs">
                    <li class="nav-item active">
                        <a class="nav-link" data-toggle="tab" href="#main">Main</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#localized">Localized</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#params">Parameters</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="#admin">Admin</a>
                    </li>
                </ul>

                <c:url var="productSave" value="/admin/products/save.html"/>
                <form:form method="POST" modelAttribute="product" action="${productSave}" enctype="multipart/form-data">

                    <form:errors path="*" cssClass="alert alert-error" element="div"/>
                    <div id="store.success" class="alert alert-success" style="
                    <c:choose>
                    <c:when test="${success!=null}">display:block;</c:when>
                    <c:otherwise>display:none;</c:otherwise>
                            </c:choose>">
                        <s:message code="message.success" text="Request successfull"/>
                    </div>
                    <div id="store.error" class="alert alert-error" style="display:none;"><s:message
                            code="message.error" text="An error occured"/></div>

                    <!-- Tab panes -->
                    <div class="tab-content">
                        <div class="tab-pane active container" id="main">

                            <form:hidden path="uuid"/>
                            <form:hidden path="availability.region"/>
                            <form:hidden path="availability.uuid"/>
                            <form:hidden path="price.uuid"/>

                            <div class="control-group">
                                <label><s:message code="label.product.sku" text="Sku"/></label>
                                <div class="controls">
                                    <form:input cssClass="input-large highlight" id="sku" path="sku"/>
                                    <span class="help-inline"><s:message code="label.generic.alphanumeric"
                                                                         text="Alphanumeric"/><form:errors
                                            path="sku" cssClass="error"/></span>
                                </div>
                            </div>
                            <div class="control-group">
                                <label><s:message code="label.sefurl" text="Sef Url"/></label>
                                <div class="controls">
                                    <form:input cssClass="input-large highlight" id="sefUrl" path="seUrl"/>
                                    <span class="help-inline"><form:errors
                                            path="seUrl" cssClass="error"/></span>
                                </div>
                            </div>
                            <div class="control-group">
                                <label><s:message code="label.product.available" text="Product available"/></label>
                                <div class="controls">
                                    <form:checkbox path="available"/>
                                </div>
                            </div>
                            <div class="control-group">
                                <label><s:message code="label.product.preorder" text="Pre-order"/></label>
                                <div class="controls">
                                    <form:checkbox path="preOrder"/>
                                </div>
                            </div>
                            <div class="control-group">
                                <label><s:message code="label.product.manufacturer" text="Manufacturer"/></label>
                                <div class="controls">
                                    <form:select items="${manufacturers}" itemValue="uuid" itemLabel="name"
                                                 path="manufacturer.uuid"/>
                                    <span class="help-inline"></span>
                                </div>
                            </div>
                            <div class="control-group">
                                <label><s:message code="label.productedit.producttype" text="Product type"/></label>
                                <div class="controls">
                                    <form:select items="${productTypes}" itemValue="uuid" itemLabel="code"
                                                 path="type.uuid"/>
                                    <span class="help-inline"></span>
                                </div>
                            </div>

                            <div class="control-group">

                                <label><s:message code="label.productedit.qtyavailable"
                                                  text="Quantity available"/></label>
                                <div class="controls">
                                    <form:input id="quantity" cssClass="highlight" path="availability.productQuantity"/>
                                    <span class="help-inline"><form:errors path="availability.productQuantity"
                                                                           cssClass="error"/></span>
                                </div>
                            </div>
                            <div class="control-group">
                                <label><s:message code="label.product.ordermin" text="Quantity order minimum"/></label>
                                <div class="controls">
                                    <form:input id="ordermin" cssClass="highlight"
                                                path="availability.productQuantityOrderMin"/>
                                    <span class="help-inline"><form:errors path="availability.productQuantityOrderMin"
                                                                           cssClass="error"/></span>

                                </div>
                            </div>
                            <div class="control-group">
                                <label><s:message code="label.product.ordermax" text="Quantity order maximum"/></label>
                                <div class="controls">
                                    <form:input id="ordermax" cssClass="highlight"
                                                path="availability.productQuantityOrderMax"/>
                                    <span class="help-inline"><form:errors path="availability.productQuantityOrderMax"
                                                                           cssClass="error"/></span>
                                </div>
                            </div>

                            <div class="control-group">
                                <label><s:message code="label.product.shipeable"
                                                  text="Product will be shipped"/></label>
                                <div class="controls">
                                    <form:checkbox path="productShippable"/>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane container" id="params">
                            <div class="control-group">

                                <label class="required"><s:message code="label.product.price" text="Price"/></label>
                                <div class="controls">
                                    <form:input id="productPriceAmount" cssClass="highlight" path="productPrice"/>
                                    <span id="help-price" class="help-inline">
                                <form:errors path="productPrice" cssClass="error"/>
                            </span>
                                </div>
                            </div>

                            <div class="control-group">
                                <label><s:message code="label.product.weight" text="Weight"/></label>
                                <div class="controls">
                                    <form:input id="weight" cssClass="" path="productWeight"/>
                                    <span class="help-inline"><form:errors path="productWeight"
                                                                           cssClass="error"/></span>
                                </div>
                            </div>

                            <div class="control-group">
                                <label><s:message code="label.product.height" text="Height"/></label>
                                <div class="controls">
                                    <form:input id="height" cssClass="" path="productHeight"/>
                                    <span class="help-inline"><form:errors path="productHeight"
                                                                           cssClass="error"/></span>
                                </div>
                            </div>


                            <div class="control-group">
                                <label><s:message code="label.product.width" text="Width"/></label>
                                <div class="controls">
                                    <form:input id="width" cssClass=""
                                                path="productWidth"/>
                                    <span class="help-inline"><form:errors path="productWidth"
                                                                           cssClass="error"/></span>
                                </div>
                            </div>

                            <div class="control-group">
                                <label><s:message code="label.product.length" text="Length"/></label>
                                <div class="controls">
                                    <form:input id="length" cssClass="" path="productLength"/>
                                    <span class="help-inline"><form:errors path="productLength"
                                                                           cssClass="error"/></span>
                                </div>
                            </div>

                            <div class="control-group">
                                <label><s:message code="label.entity.order" text="Sort order"/></label>
                                <div class="controls">
                                    <form:input id="order" cssClass="" path="sortOrder"/>
                                    <span class="help-inline"><form:errors path="sortOrder" cssClass="error"/></span>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane container" id="localized">
                            <lang:multiLanguageInput path="name"
                                                     map="${product.name}"
                                                     message="label.productedit.productname"
                                                     messageText="Product name"/>

                            <lang:multiLanguageInput path="productHighlight"
                                                     map="${product.productHighlight}"
                                                     message="label.productedit.producthl"
                                                     messageText="Product highlight"/>

                            <lang:multiLanguageTextArea inputId="product.description"
                                                        path="description"
                                                        map="${product.description}"
                                                        message="label.productedit.productdesc"
                                                        messageText="Product description"/>

                            <lang:multiLanguageInput path="metatagTitle"
                                                     map="${product.metatagTitle}"
                                                     message="label.metatags.title"
                                                     messageText="Metatag title"/>
                            <lang:multiLanguageInput path="metatagDescription"
                                                     map="${product.metatagDescription}"
                                                     message="label.metatags.description"
                                                     messageText="Metatag description"/>
                            <lang:multiLanguageInput path="metatagKeywords"
                                                     map="${product.metatagKeywords}"
                                                     message="label.metatags.keywords"
                                                     messageText="Metatag keywords"/>
                        </div>
                        <div class="tab-pane container" id="admin">
                            <div class="control-group">
                                <label><s:message code="label.product.availabledate" text="Date available"/></label>
                                <div class="controls">
                                    <input id="dateAvailable" name="dateAvailable" value="${product.dateAvailable}"
                                           class="small" type="text"
                                           data-date-format="${Constants.DEFAULT_DATE_FORMAT}"
                                           data-datepicker="datepicker">
                                    <script type="text/javascript">
                                        $('#dateAvailable').datepicker();
                                    </script>
                                    <span class="help-inline"><form:errors path="dateAvailable"
                                                                           cssClass="error"/></span>
                                </div>
                            </div>

                            <div class="control-group">
                                <label><s:message code="label.product.image" text="Image"/>&nbsp;<c:if
                                        test="${product.productImage.productImage!=null && product.productImage.productImage!=''}"><span
                                        id="imageControlRemove"> - <a href="#"
                                                                      onClick="removeImage('${product.productImage.uuid}')"><s:message
                                        code="label.generic.remove" text="Remove"/></a></span></c:if></label>
                                <div class="controls" id="imageControl">
                                    <c:choose>
                                        <c:when test="${product.productImage.productImage==null || product.productImage.productImage==''}">
                                            <input class="input-file" id="image" name="image" type="file">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="<sm:productImage imageName="${product.productImage.productImage}" product="${productItem}"/>"
                                                 width="200"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <form:hidden path="productImage.productImage"/>
                        </div>
                    </div>

                    <div class="form-actions">
                        <div class="pull-right">
                            <button type="submit" class="btn btn-success"><s:message code="button.label.submit2"
                                                                                     text="Submit"/></button>
                        </div>
                    </div>


                </form:form>

                <c:if test="${product.uuid!=null}">
                    <c:url var="createSimilar" value="/admin/products/product/duplicate.html"/>
                    <form:form method="POST" enctype="multipart/form-data" modelAttribute="product"
                               action="${createSimilar}">
                        <input type="hidden" name="productId" value="${product.uuid}"/>
                        <div class="form-actions">
                            <div class="pull-right">
                                <button type="submit" class="btn"><s:message code="label.product.createsimilar"
                                                                             text="Create similar product"/></button>
                            </div>
                        </div>

                    </form:form>
                </c:if>


            </div>


        </div>


    </div>

</div>