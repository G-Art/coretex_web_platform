<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="ERROR_MESSAGES" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="WARNING_MESSAGES" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="INFO_MESSAGES" type="java.util.List<java.lang.String>"--%>

<c:set var="errors" value="${ERROR_MESSAGES}"/>
<c:set var="warnings" value="${WARNING_MESSAGES}"/>
<c:set var="infos" value="${INFO_MESSAGES}"/>
<c:if test="${(not empty errors) || (not empty warnings) || (not empty infos)}">

    <c:if test="${not empty infos}">
        <div class="alert alert-success background-success opa" style="margin-bottom: 2px">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <i class="icofont icofont-close-line-circled text-white"></i>
            </button>
            <strong>Info:</strong>
            <c:forEach items="${infos}" var="msg" varStatus="status">
                <p class="m-b-0 m-l-35">${msg}</p>
                <c:if test="${!status.last}">
                    <hr/>
                </c:if>
            </c:forEach>

        </div>
    </c:if>

    <c:if test="${not empty warnings}">
        <div class="alert alert-warning background-warning" style="margin-bottom: 2px">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <i class="icofont icofont-close-line-circled text-white"></i>
            </button>
            <strong>Warning:</strong>
            <c:forEach items="${warnings}" var="msg" varStatus="status">
                <p class="m-b-0 m-l-35">${msg}</p>
                <c:if test="${!status.last}">
                    <hr/>
                </c:if>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger background-danger" style="margin-bottom: 2px">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <i class="icofont icofont-close-line-circled text-white"></i>
            </button>
            <strong>Error:</strong>
            <c:forEach items="${errors}" var="msg" varStatus="status">
                <p class="m-b-0 m-l-35">${msg}</p>
                <c:if test="${!status.last}">
                    <hr/>
                </c:if>
            </c:forEach>
        </div>
    </c:if>

</c:if>

