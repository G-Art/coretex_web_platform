<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>


<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="expires" content="0">
    <title><s:message code="label.storeadministration"
                      text="Store administration"/>
    </title>


    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="<c:url value="/resources/admin/images/icons/favicon.ico" />"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/vendor/bootstrap/css/bootstrap.min.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/fonts/font-awesome-4.7.0/css/font-awesome.min.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/fonts/Linearicons-Free-v1.0.0/icon-font.min.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/vendor/animate/animate.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/vendor/css-hamburgers/hamburgers.min.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/vendor/animsition/css/animsition.min.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/vendor/select2/select2.min.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/vendor/daterangepicker/daterangepicker.css" />">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/css/util.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/admin/css/main.css" />">
    <!--===============================================================================================-->

</head>

<body>


<div class="limiter">
    <div class="container-login100" style="background-image: url('<c:url value="/resources/admin/images/bg-01.jpg" />');">
        <div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
					<s:message code="button.label.logon" text="Logon"/>
				</span>
            <form method="post" class="login100-form validate-form p-b-33 p-t-5" action="<c:url value="/admin/login"/>">

                <div class="wrap-input100 validate-input" data-validate = "Enter username">
                    <input class="input100" type="text" id="username" name="username"placeholder="<s:message code="label.username" text="Username"/>">
                    <span class="focus-input100" data-placeholder="&#xe82a;"></span>
                </div>

                <div class="wrap-input100 validate-input" data-validate="Enter password">
                    <input class="input100" type="password" id="password" name="password" placeholder="<s:message code="label.password" text="Password"/>">
                    <span class="focus-input100" data-placeholder="&#xe80f;"></span>
                </div>


                <div class="container-login100-form-btn m-t-32">
                    <button class="login100-form-btn" >
                        <s:message
                                code="button.label.logon" text="button.label.submit2"/>
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

<div id="dropDownSelect1"></div>

<!--===============================================================================================-->
<script src="<c:url value="/resources/admin/vendor/jquery/jquery-3.2.1.min.js" />"></script>
<!--===============================================================================================-->
<script src="<c:url value="/resources/admin/vendor/animsition/js/animsition.min.js" />"></script>
<!--===============================================================================================-->
<script src="<c:url value="/resources/admin/vendor/bootstrap/js/popper.js" />"></script>
<script src="<c:url value="/resources/admin/vendor/bootstrap/js/bootstrap.min.js" />"></script>
<!--===============================================================================================-->
<script src="<c:url value="/resources/admin/vendor/select2/select2.min.js" />"></script>
<!--===============================================================================================-->
<script src="<c:url value="/resources/admin/vendor/daterangepicker/moment.min.js" />"></script>
<script src="<c:url value="/resources/admin/vendor/daterangepicker/daterangepicker.js" />"></script>
<!--===============================================================================================-->
<script src="<c:url value="/resources/admin/vendor/countdowntime/countdowntime.js" />"></script>
<!--===============================================================================================-->
<script src="<c:url value="/resources/admin/js/main.js" />"></script>

</body>
</html>