<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="text" required="true" %>
<%@ attribute name="path" required="true" %>
<%@ attribute name="img_class"%>

<c:set var="active" value="${pageContext.request.contextPath.concat(path) eq requestPath}"/>
<li class="nav-item ${active ? 'active' : ''}">
    <spring:url value="${path}" var="types" />
    <a class="nav-link" href="${active ? '#' : types}">
        <i class="fas fa-fw ${img_class}"></i>
        <span>${text}</span>
    </a>
</li>