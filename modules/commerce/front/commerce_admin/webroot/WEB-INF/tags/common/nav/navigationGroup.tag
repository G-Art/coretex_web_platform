<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="name" required="true" %>

<div class="pcoded-navigatio-lavel">${name}</div>
<jsp:doBody var="bodyText"/>
<c:if test="${not empty fn:trim(bodyText)}">
    <ul class="pcoded-item pcoded-left-item">
        ${bodyText}
    </ul>
</c:if>