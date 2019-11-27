<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="name" required="true" type="java.lang.String" %>
<%@ attribute name="menuClass" required="false" type="java.lang.String" %>
<%@ attribute name="iconClass" required="false" type="java.lang.String" %>
<%@ attribute name="labelClass" required="false" type="java.lang.String" %>
<%@ attribute name="labelText" required="false" type="java.lang.String" %>

<%@ attribute name="link" required="false" type="java.lang.String" %>
<%@ attribute name="linkTarget" required="false" type="java.lang.String" %>

<%@ attribute name="showLable" required="false" type="java.lang.Boolean" %>
<%@ attribute name="trigger" required="false" type="java.lang.Boolean" %>

<c:if test="${empty trigger}" >
    <c:set var="trigger" value="false" />
</c:if>

<c:if test="${empty linkTarget}" >
    <c:set var="linkTarget" value="_self" />
</c:if>

<c:if test="${empty showLable}" >
    <c:set var="showLable" value="false" />
</c:if>

<c:set var="pathLink" value="${pageContext.request.contextPath.concat(link)}"/>
<c:set var="active" value="${pathLink eq requestPath}"/>
<jsp:doBody var="bodyText"/>

<li class="${menuClass} ${active ? 'active' : ''} ${trigger ? 'pcoded-trigger' : ''}">
    <a href="${not empty fn:trim(link) and not active ? pathLink : 'javascript:void(0)'}" target="${linkTarget}">
        <c:if test="${not empty fn:trim(iconClass)}">
            <span class="pcoded-micon"><i class=" ${iconClass}"></i></span>
        </c:if>
        <span class="pcoded-mtext">${name}</span>
        <c:if test="${showLable}">
            <span class="pcoded-badge label ${labelClass} ">${labelText}</span>
        </c:if>
    </a>
    <c:if test="${not empty fn:trim(bodyText)}">
        <ul class="pcoded-submenu">
                ${bodyText}
        </ul>
    </c:if>
</li>