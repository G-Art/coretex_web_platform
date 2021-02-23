<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="metaDescription" required="false" %>
<%@ attribute name="metaKeywords" required="false" %>

<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>

<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/template" %>
<%@ taglib prefix="common-components" tagdir="/WEB-INF/tags/common/components" %>


<template:page pageTitle="${pageTitle}">
    <jsp:attribute name="pageCss">
        <!-- Google font-->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,800" rel="stylesheet">
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/assets/icon/themify-icons/themify-icons.css"/>"/>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/assets/icon/material-design/css/material-design-iconic-font.min.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/icon/icofont/css/icofont.css"/>"/>
       <jsp:invoke fragment="pageCss"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/css/style.css"/>"/>
	</jsp:attribute>

    <jsp:attribute name="pageScripts">
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/jquery/js/jquery.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/jquery-ui/js/jquery-ui.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/popper.js/js/popper.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/bootstrap/js/bootstrap.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/modernizr/js/modernizr.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/modernizr/js/css-scrollbars.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/i18next/js/i18next.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/i18next-xhr-backend/js/i18nextXHRBackend.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/i18next-browser-languagedetector/js/i18nextBrowserLanguageDetector.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/jquery-i18next/js/jquery-i18next.min.js"/>"></script>

        <script type="text/javascript" src="<c:url value="/resources/assets/js/pcoded.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/js/menu/menu-header-fixed.js"/>"></script>
		<jsp:invoke fragment="pageScripts"/>
	</jsp:attribute>
    <jsp:body>
        <div id="pcoded" class="pcoded">
            <div class="pcoded-overlay-box"></div>
            <div class="pcoded-container navbar-wrapper">
                <common:headerNavBar/>

                <div class="pcoded-main-container">
                    <div class="pcoded-wrapper">
                        <common:leftNavBar/>
                        <div class="pcoded-content">

                            <div class="row justify-content-center">
                                <div class="col-12">
                                    <common-components:alertBlocks/>
                                </div>
                            </div>

                            <div class="pcoded-inner-content">
                                <!-- Main-body start -->
                                <div class="main-body">
                                    <div class="page-wrapper">
                                        <jsp:doBody/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <common:ieWarning/>

    </jsp:body>
</template:page>