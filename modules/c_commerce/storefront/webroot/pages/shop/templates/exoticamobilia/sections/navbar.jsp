<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="/WEB-INF/shopizer-functions.tld" prefix="display" %>


<c:set var="req" value="${request}"/>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<!-- NAVIGATION MENU -->
<div class="col-md-8">
    <div class="header-right clearfix">
        <nav class="navbar navbar-expand-lg navbar-light">
            <div class="container">

                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#main_nav"
                        aria-controls="main_nav"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="main_nav">
                    <ul class="navbar-nav">
                        <c:set var="code" value="${category.code}"/>
                        <c:forEach items="${requestScope.TOP_CATEGORIES}" var="category">
                            <li class="nav-item <c:if test="${fn:length(category.children)>0}">dropdown</c:if>">
                                <a href="<c:url value="/shop/category/${category.description.friendlyUrl}.html"/><sm:breadcrumbParam categoryId="${category.uuid}"/>"
                                   class="nav-link <c:if test="${fn:length(category.children)>0}">dropdown-toggle</c:if> <c:if test="${category.code==code}">currentSelectedLink</c:if>"
                                   <c:if test="${fn:length(category.children)>0}">data-toggle="dropdown"
                                   id="${category.uuid}"
                                   role="button" aria-haspopup="true" aria-expanded="false" </c:if>>
                                        ${category.description.name}</a>
                                <c:if test="${fn:length(category.children)>0}">
                                    <div class="dropdown-menu" aria-labelledby="${category.uuid}">
                                        <c:forEach items="${category.children}" var="child">
                                            <a class="dropdown-item" href="<c:url value="/shop/category/${child.description.friendlyUrl}.html"/><sm:breadcrumbParam categoryId="${child.uuid}"/>"><c:out
                                                    value="${child.description.name}"/></a>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </li>
                        </c:forEach>
                    </ul>
                </div> <!-- collapse .// -->
            </div> <!-- container .// -->
        </nav>
    </div>
</div>


</div>
</header>