<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/template" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<template:page pageTitle="Sign In">
    <jsp:attribute name="pageCss">
        <!--===============================================================================================-->
        <!-- Google font-->
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,800" rel="stylesheet">
        <!--=====================================/==========================================================-->
        <!-- Required Fremwork -->
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/bootstrap/css/bootstrap.min.css" />">
        <!--===============================================================================================-->
        <!-- themify-icons line icon -->
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/assets/icon/themify-icons/themify-icons.css"/>"/>
        <!--===============================================================================================-->
        <!-- ico font -->
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/icon/icofont/css/icofont.css"/>"/>
        <!--===============================================================================================-->
        <!-- Style.css -->
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/css/style.css"/>"/>
        <!--===============================================================================================-->
	</jsp:attribute>

    <jsp:attribute name="pageScripts">
		<!-- Required Jquery -->
        <script type="text/javascript" src="<c:url value="/resources/bower_components/jquery/js/jquery.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/jquery-ui/js/jquery-ui.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/bower_components/popper.js/js/popper.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/bootstrap/js/bootstrap.min.js"/>"></script>
        <!-- jquery slimscroll js -->
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/jquery-slimscroll/js/jquery.slimscroll.js"/>"></script>
        <!-- modernizr js -->
        <script type="text/javascript" src="<c:url value="/resources/bower_components/modernizr/js/modernizr.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/modernizr/js/css-scrollbars.js"/>"></script>
        <!-- i18next.min.js -->
        <script type="text/javascript" src="<c:url value="/resources/bower_components/i18next/js/i18next.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/i18next-xhr-backend/js/i18nextXHRBackend.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/i18next-browser-languagedetector/js/i18nextBrowserLanguageDetector.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/jquery-i18next/js/jquery-i18next.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/js/common-pages.js"/>"></script>
        <%--<!-- Global site tag (gtag.js) - Google Analytics -->--%>
        <%--<script async="" src="https://www.googletagmanager.com/gtag/js?id=UA-23581568-13"></script>--%>
        <%--<script>--%>
        <%--    window.dataLayer = window.dataLayer || [];--%>
        <%--    function gtag(){dataLayer.push(arguments);}--%>
        <%--    gtag('js', new Date());--%>

        <%--    gtag('config', 'UA-23581568-13');--%>
        <%--</script>--%>
	</jsp:attribute>
    <jsp:body>
        <section class="login-block">
            <!-- Container-fluid starts -->
            <div class="container">
                <div class="row">
                    <div class="col-sm-12">
                        <!-- Authentication card start -->

                        <form method="post" class="md-float-material form-material" action="<c:url value="/login"/>">
                            <div class="text-center">
                                <img src="<c:url value="/resources/assets/images/logo.png"/>" alt="logo.png">
                            </div>
                            <div class="auth-box card">
                                <div class="card-block">
                                    <div class="row m-b-20">
                                        <div class="col-md-12">
                                            <h3 class="text-center">Sign In</h3>
                                        </div>
                                    </div>
                                    <div class="form-group form-primary">
                                        <input type="text" name="login" class="form-control" required=""
                                               placeholder="<s:message code="label.username" text="Username"/>">
                                        <span class="form-bar"></span>
                                    </div>
                                    <div class="form-group form-primary">
                                        <input type="password" name="password" class="form-control" required=""
                                               placeholder="<s:message code="label.password" text="Password"/>">
                                        <span class="form-bar"></span>
                                    </div>
                                    <div class="row m-t-30">
                                        <div class="col-md-12">
                                            <button type="submit"
                                                    class="btn btn-primary btn-md btn-block waves-effect waves-light text-center m-b-20">
                                                Sign in
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                        <!-- end of form -->
                    </div>
                    <!-- end of col-sm-12 -->
                </div>
                <!-- end of row -->
            </div>
            <!-- end of container-fluid -->
        </section>
        <common:ieWarning/>
    </jsp:body>

</template:page>