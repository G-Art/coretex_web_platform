<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>

<%@attribute name="headerStyle" fragment="true" required="false" %>
<%@attribute name="headerJs" fragment="true" %>
<%@attribute name="footerJs" fragment="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>

<template:root pageTitle="${pageTitle}">
    <jsp:attribute name="headerStyle">
      <jsp:invoke fragment="headerStyle"/>
    </jsp:attribute>
    <jsp:attribute name="headerJs">
      <jsp:invoke fragment="headerJs"/>
    </jsp:attribute>
    <jsp:attribute name="footerJs">
      <jsp:invoke fragment="footerJs"/>
        <script>
            $(function () {
                $('[data-toggle="tooltip"]').tooltip()
            })
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="container-fluid">
            <jsp:doBody/>
        </div>
    </jsp:body>

</template:root>
