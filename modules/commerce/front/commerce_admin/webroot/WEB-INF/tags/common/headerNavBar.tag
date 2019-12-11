<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<nav class="navbar header-navbar pcoded-header" >
    <div class="navbar-wrapper d-flex">

        <div class="navbar-logo" >
            <a class="mobile-menu" id="mobile-collapse" href="javascript:void(0);">
                <i class="feather icon-menu"></i>
            </a>
            <a href="<c:url value="/"/>" class="d-sm-none d-md-none d-xl-inline d-lg-inline">
                <img class="img-fluid" src="<c:url value="/resources/assets/images/logo.png"/>" alt="Theme-Logo">
            </a>
            <a class="mobile-options">
                <i class="feather icon-more-horizontal"></i>
            </a>
        </div>

        <div class="navbar-container container-fluid">
            <ul class="nav-left">
                <li>
                    <a href="javascript:void(0);" onclick="javascript:toggleFullScreen()">
                        <i class="feather icon-maximize full-screen"></i>
                    </a>
                </li>
            </ul>
            <ul class="nav-right">
                <li class="user-profile header-notification">
                    <common:userProfileHeaderSection/>
                </li>
            </ul>
        </div>
    </div>
</nav>