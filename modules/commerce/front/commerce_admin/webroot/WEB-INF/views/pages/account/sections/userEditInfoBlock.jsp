<%--@elvariable id="user" type="com.coretex.commerce.data.UserData"--%>
<%--@elvariable id="currentUser" type="com.coretex.commerce.data.UserData"--%>
<%--@elvariable id="languages" type="java.util.List<com.coretex.commerce.data.LocaleData>"--%>
<%--@elvariable id="stores" type="java.util.List<com.coretex.commerce.data.StoreData>"--%>
<%--@elvariable id="groups" type="java.util.List<com.coretex.commerce.data.GroupData>"--%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tags-account" tagdir="/WEB-INF/tags/account" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!-- end of view-info -->
<div class="edit-info">
    <form id="userForm" method="post" action="<c:url value="/user/account/save"/>">
        <div class="row">
            <div class="col-lg-12">
                <div class="general-info">
                    <div class="row">
                        <div class="col-lg-6">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="icofont icofont-user"></i>
                                                </span>
                                            </div>
                                            <input type="text" name="firstName" class="form-control"
                                                   value="${user.firstName}"
                                                   placeholder="First Name">
                                            <input type="text" name="lastName" class="form-control"
                                                   value="${user.lastName}"
                                                   placeholder="Last Name">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="icofont icofont-ui-email"></i>
                                                </span>
                                            </div>
                                            <input type="email" name="email" class="form-control" value="${user.email}"
                                                   placeholder="Email">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="icofont icofont-id-card"></i>
                                                </span>
                                            </div>
                                            <input type="text" name="login" class="form-control"
                                                   value="${user.login}"
                                                   placeholder="Login">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="icofont icofont-globe-alt"></i>
                                                </span>
                                            </div>
                                            <select name="language" class="form-control">
                                                <option value=""></option>
                                                <c:forEach var="lang" items="${languages}">
                                                    <option ${user.language.uuid eq lang.uuid ? 'selected' : ''}
                                                            value="${lang.uuid}">${lang.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <!-- end of table col-lg-6 -->
                        <div class="col-lg-6">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td>
                                        <div class="input-group">
                                            <c:choose>
                                                <c:when test="${user.uuid == currentUser.uuid}">
                                                    <input name="active" type="checkbox"
                                                           class="js-success" ${user.active ? 'checked' : ''}>
                                                </c:when>
                                                <c:otherwise>
                                                    <sec:authorize
                                                            access="hasRole('SUPERADMIN') and fullyAuthenticated">
                                                        <input name="active" type="checkbox"
                                                               class="js-success" ${user.active ? 'checked' : ''}>
                                                    </sec:authorize>
                                                    <sec:authorize access="!hasRole('SUPERADMIN')">
                                                        <tags-common:permissionDenninLable/>
                                                    </sec:authorize>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="zmdi zmdi-store"></i>
                                                </span>
                                            </div>
                                            <div class="d-flex flex-grow-1">
                                                <select name="store" class="form-control js-select-store">
                                                    <c:forEach var="store" items="${stores}">
                                                        <option ${store.uuid == user.store.uuid ? 'selected' : ''}
                                                                value="${store.uuid}">${store.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="input-group ">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="icofont icofont-ui-user-group"></i>
                                                </span>
                                            </div>
                                            <div class="d-flex flex-grow-1">
                                                <select name="groups" class="custom-select js-select-group" multiple="multiple">
                                                    <c:forEach var="group" items="${groups}">
                                                        <sec:authorize access="hasRole('SUPERADMIN')">
                                                            <c:if test="${group.groupName eq 'SUPERADMIN'}">
                                                                <c:set var="selected"
                                                                       value="${user.groups.stream().anyMatch(g -> g.uuid.equals(group.uuid)).get() }"/>
                                                                <option ${selected ? 'selected' : ''}
                                                                        value="${group.uuid}">${group.groupName}</option>
                                                            </c:if>
                                                        </sec:authorize>
                                                        <c:if test="${group.groupName ne 'SUPERADMIN'}">
                                                            <c:set var="selected"
                                                                   value="${user.groups.stream().anyMatch(g -> g.uuid.equals(group.uuid)).get() }"/>
                                                            <option ${selected ? 'selected' : ''}
                                                                    value="${group.uuid}">${group.groupName}</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <!-- end of table col-lg-6 -->
                    </div>
                    <!-- end of row -->
                    <div class="text-center">
                        <button type="submit"
                                class="btn btn-primary waves-effect waves-light m-r-20">Save
                        </button>
                        <a href="javascript:void(0);" id="edit-cancel"
                           class="btn btn-default waves-effect">Cancel</a>
                    </div>
                </div>
                <!-- end of edit info -->
            </div>
            <!-- end of col-lg-12 -->
        </div>
        <!-- end of row -->
        <input type="hidden" name="uuid" value="${user.uuid}">
    </form>

</div>
<!-- end of edit-info -->
