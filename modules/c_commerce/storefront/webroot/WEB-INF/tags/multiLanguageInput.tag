<%--@elvariable id="locales" type="java.util.List<com.coretex.items.core.LocaleItem>"--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="message" required="true" type="java.lang.String" %>
<%@ attribute name="messageText" required="true" type="java.lang.String" %>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="map" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>

<div class="control-group">
    <c:choose>
        <c:when test="${locales != null}">
            <c:forEach items="${locales}" var="lang"
                       varStatus="status">

                <label><s:message code="${message}" text="${messageText}"/> [${lang.iso}]</label>
                <div class="controls">
                    <form:input path="${path}['${lang.iso}']"/>
                </div>
                <jsp:doBody/>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach items="${map}" var="lang"
                       varStatus="status">

                <label><s:message code="${message}" text="${messageText}"/> - (${lang.key})</label>
                <div class="controls">
                    <form:input path="${path}['${lang.key}']"/>
                </div>
                <jsp:doBody/>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>