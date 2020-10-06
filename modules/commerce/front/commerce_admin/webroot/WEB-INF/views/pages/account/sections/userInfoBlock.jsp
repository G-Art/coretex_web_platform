<%--@elvariable id="user" type="com.coretex.commerce.data.UserData"--%>
<%--@elvariable id="currentUser" type="com.coretex.commerce.data.UserData"--%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tags-account" tagdir="/WEB-INF/tags/account" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="view-info">
    <div class="row">
        <div class="col-lg-12">
            <div class="general-info">
                <div class="row">
                    <div class="col-lg-12 col-xl-6">
                        <div class="table-responsive">
                            <table class="table m-0">
                                <tbody>
                                <tr>
                                    <th scope="row">Full Name</th>
                                    <td>${user.firstName}&nbsp;${user.lastName}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Email</th>
                                    <td>${user.email}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Login</th>
                                    <td>${user.login}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Default language</th>
                                    <td>${user.language.name}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- end of table col-lg-6 -->
                    <div class="col-lg-12 col-xl-6">
                        <div class="table-responsive">
                            <table class="table">
                                <tbody>

                                <tr>
                                    <th scope="row">Active</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.uuid == currentUser.uuid}">
                                                <tags-common:activeLable active="${user.active}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <sec:authorize access="hasRole('SUPERADMIN') and fullyAuthenticated">
                                                    <tags-common:activeLable active="${user.active}"/>
                                                </sec:authorize>
                                                <sec:authorize access="!hasRole('SUPERADMIN')">
                                                    <tags-common:permissionDenninLable/>
                                                </sec:authorize>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Store</th>
                                    <td>
                                        <label class="label label-inverse-info-border">${user.store.name}</label>
                                    </td>
                                </tr>

                                <c:choose>
                                    <c:when test="${user.uuid == currentUser.uuid}">
                                        <tr>
                                            <th scope="row">Groups</th>
                                            <td>
                                                <c:forEach var="group" items="${user.groups}">
                                                    <label class="label bg-primary">${group.groupName}</label>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <sec:authorize access="hasRole('SUPERADMIN') and fullyAuthenticated">
                                            <tr>
                                                <th scope="row">Groups</th>
                                                <td>
                                                    <c:forEach var="group" items="${user.groups}">
                                                        <label class="label bg-primary">${group.groupName}</label>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </sec:authorize>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- end of table col-lg-6 -->
                </div>
                <!-- end of row -->
            </div>
            <!-- end of general info -->
        </div>
        <!-- end of col-lg-12 -->
    </div>
    <!-- end of row -->
</div>