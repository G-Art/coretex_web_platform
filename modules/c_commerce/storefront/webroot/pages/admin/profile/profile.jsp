<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page session="false" %>


<script type="text/javascript">


    $(function () {
        if ($("#adminName").val() == "") {
            $('.btn').addClass('disabled');
        }
    });


    function validateCode() {
        $('#checkCodeStatus').html('<img src="<c:url value="/resources/img/ajax-loader.gif" />');
        $('#checkCodeStatus').show();
        var adminName = $("#adminName").val();
        var id = $("#id").val();
        checkCode(adminName, id, '<c:url value="/admin/users/checkUserCode.html" />');
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

    <div class="tab-content">

        <div class="tab-pane active" id="catalogue-section">


            <div class="sm-ui-component">


                <h3>
                    <c:choose>
                        <c:when test="${user.uuid!=null}">
                            <s:message code="label.user.edituser" text="Edit user"/> <c:out value="${user.firstName}"/>
                        </c:when>
                        <c:otherwise>
                            <s:message code="label.user.createuser" text="Create user"/>
                        </c:otherwise>
                    </c:choose>

                </h3>
                <br/>

                <c:url var="userSave" value="/admin/users/save.html"/>


                <%--@elvariable id="user" type="com.coretex.shop.admin.forms.UserForm"--%>
                <form:form method="POST" modelAttribute="user" action="${userSave}">


                    <form:errors path="*" cssClass="alert alert-error" element="div"/>
                    <div id="store.success" class="alert alert-success" style="
                    <c:choose>
                    <c:when test="${success!=null}">display:block;</c:when>
                        <c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success"
                                                                                        text="Request successfull"/></div>


                    <div class="control-group">
                        <label><s:message code="label.store.title" text="Store"/> </label>
                        <div class="controls">
                            <form:select cssClass="" items="${stores}" itemValue="uuid" itemLabel="code"
                                         path="merchantStore.uuid"/>
                            <span class="help-inline"><form:errors path="merchantStore" cssClass="error"/></span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label><s:message code="label.user.email" text="Email"/></label>
                        <div class="controls">
                            <form:input cssClass="input-large highlight" path="email"/>
                            <span class="help-inline"><form:errors path="email" cssClass="error"/></span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label><s:message code="label.user.login" text="Login"/></label>
                        <div class="controls">
                            <form:input cssClass="input-large highlight" path="adminName"/>
                            <span class="help-inline"><form:errors path="email" cssClass="error"/></span>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${user.uuid==null }">
                            <div class="control-group">
                                <label><s:message code="label.user.password" text="Password"/></label>
                                <div class="controls">
                                    <form:password cssClass="input-large highlight" path="password"/>
                                    <span class="help-inline"><form:errors path="password" cssClass="error"/></span>
                                </div>

                            </div>
                        </c:when>
                        <c:otherwise>
                            <sec:authorize access="hasRole('SUPERADMIN') and fullyAuthenticated">
                                <div class="control-group">
                                    <label><s:message code="label.user.password" text="Password"/></label>
                                    <div class="controls">
                                        <form:password cssClass="input-large highlight" path="password"/>
                                        <span class="help-inline"><form:errors path="password" cssClass="error"/></span>
                                    </div>

                                </div>
                            </sec:authorize>
                        </c:otherwise>
                    </c:choose>


                    <div class="control-group">
                        <label><s:message code="label.user.firstName" text="First name"/> </label>
                        <div class="controls">
                            <form:input cssClass="input-large" path="firstName"/>
                            <span class="help-inline"><form:errors path="firstName" cssClass="error"/></span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label><s:message code="label.user.lastName" text="Last name"/> </label>
                        <div class="controls">
                            <form:input cssClass="input-large" path="lastName"/>
                            <span class="help-inline"><form:errors path="lastName" cssClass="error"/></span>
                        </div>
                    </div>

                    <div class="control-group">
                        <label><s:message code="label.user.defaultLanguage" text="Default language"/> </label>
                        <div class="controls">
                            <form:select cssClass="" items="${languages}" itemValue="uuid" itemLabel="code"
                                         path="language.uuid"/>
                            <span class="help-inline"><form:errors path="language" cssClass="error"/></span>
                        </div>
                    </div>

                    <sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
                        <div class="control-group">
                            <label><s:message code="label.groups.title" text="Groups"/></label>
                            <div class="controls">
                                <form:checkboxes cssClass="highlight" items="${groups}" itemValue="uuid"
                                                 itemLabel="groupName" path="groups"  delimiter="<br/>"/>
                                <span class="help-inline"><form:errors path="groups" cssClass="error"/></span>
                            </div>
                        </div>
                    </sec:authorize>


                    <sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
                        <div class="control-group">
                            <label>
                                <strong><s:message code="label.entity.active" text="Active"/></strong>
                            </label>
                            <div class="controls">
                                <form:checkbox path="active"/>
                            </div>
                        </div>
                    </sec:authorize>

                    <form:hidden path="uuid"/>

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