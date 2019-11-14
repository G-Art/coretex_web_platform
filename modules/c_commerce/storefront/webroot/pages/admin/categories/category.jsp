<%--@elvariable id="category" type="com.coretex.shop.admin.forms.CategoryForm"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="lang" tagdir="/WEB-INF/tags/lang" %>
<%@ page session="false" %>


<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>


<script type="text/javascript">


    $(function () {
        $('#order').numeric();
        if ($("#code").val() == "") {
            $('.btn').addClass('disabled');
        }
        $("#name").friendurl({id: 'seUrl'});
    });


    function validateCode() {
        $('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
        $('#checkCodeStatus').show();
        var code = $("#code").val();
        var id = $("#id").val();
        checkCode(code, id, '<c:url value="/admin/categories/checkCategoryCode.html" />');
    }

    function callBackCheckCode(msg, code) {
        console.log(code);
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

    <div class="tab-content">
        <div class="tab-pane active" id="catalogue-section">
            <div class="sm-ui-component">
                <h3>
                    <c:choose>
                        <c:when test="${category.uuid!=null }">
                            <s:message code="label.category.editcategory" text="Edit category"/>
                            (<c:out value="${category.code}"/>)
                        </c:when>
                        <c:otherwise>
                            <s:message code="label.category.createcategory" text="Create category"/>
                        </c:otherwise>
                    </c:choose>

                </h3>
                <br/>
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

                <c:url var="categorySave" value="/admin/categories/save.html"/>

                <form:form method="POST" modelAttribute="category" action="${categorySave}">


                    <form:errors path="*" cssClass="alert alert-error" element="div"/>
                    <div id="store.success" class="alert alert-success" style="
                    <c:choose>
                    <c:when test="${success!=null}">display:block;</c:when>
                    <c:otherwise>display:none;</c:otherwise>
                            </c:choose>">
                        <s:message code="message.success"
                                   text="Request successfull"/>
                    </div>

                    <div class="tab-content">
                        <div class="tab-pane active container" id="main">
                            <form:hidden path="uuid"/>
                            <div class="control-group">
                                <label><s:message code="label.category.code" text="Category code"/></label>
                                <div class="controls">
                                    <form:input cssClass="input-large highlight" path="code" onblur="validateCode()"/>
                                    <span class="help-inline">
                                        <div id="checkCodeStatus" style="display:none;"></div>
                                        <form:errors path="code" cssClass="error"/>
                                    </span>
                                </div>
                            </div>


                            <div class="control-group">
                                <label><s:message code="label.category.parentcategory" text="Category vsible"/></label>
                                <div class="controls">
                                    <div class="controls">
                                        <s:message code="label.category.root" text="Root" var="rootVar"/>
                                        <form:select path="parent.uuid">
                                            <form:option value="" label="${rootVar}"/>
                                            <form:options items="${categories}" itemValue="uuid"
                                                          itemLabel="name"/>
                                        </form:select>
                                        <span class="help-inline"><form:errors path="parent.uuid"
                                                                               cssClass="error"/></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane container" id="params">
                            <div class="control-group">
                                <label><s:message code="label.sefurl" text="Sef Url"/></label>
                                <div class="controls">
                                    <form:input cssClass="input-large highlight" id="sefUrl" path="seUrl"/>
                                    <span class="help-inline"><form:errors
                                            path="seUrl" cssClass="error"/></span>
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
                                                     map="${category.name}"
                                                     message="label.productedit.categoryname"
                                                     messageText="Category name"/>
                            <lang:multiLanguageInput path="categoryHighlight"
                                                     map="${category.categoryHighlight}"
                                                     message="label.category.highlight"
                                                     messageText="Category highlight"/>
                            <lang:multiLanguageTextArea path="description"
                                                        map="${category.description}"
                                                        message="label.category.categorydescription"
                                                        messageText="Category description"/>
                            <lang:multiLanguageInput path="metatagTitle"
                                                     map="${category.metatagTitle}"
                                                     message="label.category.title"
                                                     messageText="Metatag title"/>
                            <lang:multiLanguageInput path="metatagKeywords"
                                                     map="${category.metatagKeywords}"
                                                     message="label.metatags.keywords"
                                                     messageText="Metatag keywords"/>
                            <lang:multiLanguageInput path="metatagDescription"
                                                     map="${category.metatagDescription}"
                                                     message="label.metatags.description"
                                                     messageText="Metatag description"/>
                        </div>
                        <div class="tab-pane container" id="admin">
                            <div class="control-group">
                                <label><s:message code="label.entity.visible" text="Visible"/></label>
                                <div class="controls">
                                    <form:checkbox path="visible"/>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-actions">
                        <div class="pull-right">
                            <button type="submit" class="btn btn-success"><s:message code="button.label.submit"
                                                                                     text="Submit"/></button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>