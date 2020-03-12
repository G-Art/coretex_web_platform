<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="title" required="false" rtexprvalue="true" %>
<%@ attribute name="description" required="false" %>
<%@ attribute name="fullCardButton" required="false" type="java.lang.Boolean" %>
<%@ attribute name="minimizeCardButton" required="false" type="java.lang.Boolean" %>

<%@ attribute name="cardBlock" required="true" fragment="true" %>
<%@ attribute name="cardHeader" required="false" fragment="true" %>

<c:if test="${empty fullCardButton}">
    <c:set var="fullCardButton" value="true"/>
</c:if>
<c:if test="${empty minimizeCardButton}">
    <c:set var="minimizeCardButton" value="true"/>
</c:if>

<div class="card">
    <c:if test="${not empty title}">
        <div class="card-header">
            <h5>${title}</h5>
            <span>${description}</span>
            <c:if test="${fullCardButton || minimizeCardButton}">
                <div class="card-header-right">
                    <ul class="list-unstyled card-option">
                        <c:if test="${fullCardButton}">
                            <li><i class="feather icon-maximize full-card"></i></li>
                        </c:if>
                        <c:if test="${minimizeCardButton}">
                            <li><i class="feather icon-minus minimize-card"></i></li>
                        </c:if>
                            <%--                                    <li><i class="feather icon-trash-2 close-card"></i></li>--%>
                    </ul>
                </div>
            </c:if>
            <jsp:invoke fragment="cardHeader"/>
        </div>
    </c:if>

    <div class="card-block">
        <jsp:invoke fragment="cardBlock"/>
    </div>

</div>