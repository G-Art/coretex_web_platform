<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="message" required="true" type="java.lang.String" %>
<%@ attribute name="messageText" required="true" type="java.lang.String" %>
<%@ attribute name="map" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>

<div class="control-group">
    <c:forEach items="${map}" var="lang"
               varStatus="status">

        <label><s:message code="${message}" text="${messageText}"/> - (${lang.key})</label>
        <div class="controls">
            ${map[lang.key]}
        </div>

    </c:forEach>
</div>