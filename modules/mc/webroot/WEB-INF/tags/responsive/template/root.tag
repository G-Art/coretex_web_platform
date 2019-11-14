<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>

<%@attribute name="headerJs" fragment="true" %>
<%@attribute name="footerJs" fragment="true" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/template/nav" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Manager Console (${pageTitle})</title>
    <%--<link rel="icon" href="../../favicon.ico">--%>

    <link type="text/css" rel="stylesheet" href="//unpkg.com/bootstrap-vue@latest/dist/bootstrap-vue.css"/>

    <script src="https://cdn.jsdelivr.net/npm/vue"></script>

    <link href="<c:url value="/resources/vendor/fontawesome-free/css/all.min.css"/>" rel="stylesheet" type="text/css">

    <!-- Page level plugin CSS-->
    <link href="<c:url value="/resources/vendor/datatables/dataTables.bootstrap4.css"/>" rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="<c:url value="/resources/css/sb-admin.css"/>" rel="stylesheet">

    <jsp:invoke fragment="headerJs"/>
<body id="page-top">
<nav class="navbar navbar-expand navbar-dark bg-dark static-top">

    <a class="navbar-brand mr-1" href="${pageContext.request.contextPath}">Core Tex</a>

    <button class="btn btn-link btn-sm text-white order-1 order-sm-0" id="sidebarToggle" href="#">
        <i class="fas fa-bars"></i>
    </button>

</nav>
<div id="wrapper">
    <nav:consoleMenu/>
    <!-- Begin page content -->
    <div id="content-wrapper">
        <jsp:doBody/>
        <!-- Sticky Footer -->
        <footer class="sticky-footer">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Current context ${requestPath}</span>
                </div>
            </div>
        </footer>

    </div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="<c:url value="/resources/vendor/jquery/jquery.min.js"/>"></script>
<script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"/>"></script>

<!-- Core plugin JavaScript-->
<script src="<c:url value="/resources/vendor/jquery-easing/jquery.easing.min.js"/>"></script>

<!-- Page level plugin JavaScript-->
<script src="<c:url value="/resources/vendor/chart.js/Chart.min.js"/>"></script>
<script src="<c:url value="/resources/vendor/datatables/jquery.dataTables.js"/>"></script>
<script src="<c:url value="/resources/vendor/datatables/dataTables.bootstrap4.js"/>"></script>

<!-- Custom scripts for all pages-->
<script src="<c:url value="/resources/js/sb-admin.min.js"/>"></script>

<!-- Demo scripts for this page-->
<script src="<c:url value="/resources/js/demo/datatables-demo.js"/>"></script>
<%--<script src="<c:url value="/resources/js/demo/chart-area-demo.js"/>"></script>--%>

<script src="//unpkg.com/babel-polyfill@latest/dist/polyfill.min.js"></script>
<script src="//unpkg.com/bootstrap-vue@latest/dist/bootstrap-vue.js"></script>
<jsp:invoke fragment="footerJs"/>

</body>
</html>
