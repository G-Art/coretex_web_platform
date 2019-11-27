<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<div class="dropdown-primary dropdown">
    <div class="dropdown-toggle" data-toggle="dropdown">
        <img src="<c:url value="/resources/assets/images/social/profile.jpg"/>" class="img-radius" alt="User-Profile-Image">
        <sec:authentication property="principal.username" var="login"/>
        <sec:authentication property="principal.firstName" var="firstName"/>
        <sec:authentication property="principal.lastName" var="lastName"/>
        <span>
            <c:choose>
                <c:when test="${not empty firstName}">
                    ${firstName} ${lastName}
                </c:when>
                <c:otherwise>
                    ${login}
                </c:otherwise>
            </c:choose>
        </span>
        <i class="feather icon-chevron-down"></i>
    </div>
    <ul class="show-notification profile-notification dropdown-menu" data-dropdown-in="fadeIn" data-dropdown-out="fadeOut">
<%--        <li>--%>
<%--            <a href="javascript:void(0);">--%>
<%--                <i class="feather icon-settings"></i> Settings--%>
<%--            </a>--%>
<%--        </li>--%>
        <li>
            <a href="<c:url value="/user/account"/>">
                <i class="feather icon-user"></i> Profile
            </a>
        </li>
<%--        <li>--%>
<%--            <a href="auth-lock-screen.htm">--%>
<%--                <i class="feather icon-lock"></i> Lock Screen--%>
<%--            </a>--%>
<%--        </li>--%>
        <li>
            <a href="<c:url value="/logout"/>">
                <i class="feather icon-log-out"></i> Logout
            </a>
        </li>
    </ul>

</div>