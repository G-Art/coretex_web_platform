<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="metaDescription" required="false" %>
<%@ attribute name="metaKeywords" required="false" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html lang="en">

<head>
    <title>
        ${not empty pageTitle ? pageTitle : "Title"}
    </title>
    <!-- HTML5 Shim and Respond.js IE10 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 10]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript"
            src="<c:url value="/resources/bower_components/jquery/js/jquery.min.js"/>"></script>
    <script type="text/javascript"
            src="<c:url value="/resources/bower_components/jquery-slimscroll/js/jquery.slimscroll.js"/>"></script>
    <script type="text/javascript"
            src="<c:url value="/resources/bower_components/jquery-ui/js/jquery-ui.min.js"/>"></script>

    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="${metaDescription}">
    <meta name="keywords" content="${metaKeywords}">
    <meta name="author" content="#">

    <base href="/">
    <!--===============================================================================================-->
    <!-- Favicon icon -->
    <link rel="icon" href="<c:url value="/resources/assets/images/favicon.ico" />" type="image/x-icon">

    <%-- Inject any additional CSS required by the page --%>
    <jsp:invoke fragment="pageCss"/>

    <script>
        let scripts = new Set()
    </script>
</head>

<body class="fix-menu">
<common:themeLoader/>

<jsp:doBody/>

<%-- Inject any additional JavaScript required by the page --%>
<jsp:invoke fragment="pageScripts"/>


<%--<!-- Global site tag (gtag.js) - Google Analytics -->--%>
<%--<script async="" src="https://www.googletagmanager.com/gtag/js?id=UA-23581568-13"></script>--%>
<%--<script>--%>
<%--    window.dataLayer = window.dataLayer || [];--%>
<%--    function gtag(){dataLayer.push(arguments);}--%>
<%--    gtag('js', new Date());--%>

<%--    gtag('config', 'UA-23581568-13');--%>
<%--</script>--%>
</body>

</html>

