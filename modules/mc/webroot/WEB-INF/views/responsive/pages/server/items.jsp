<%--@elvariable id="mataTypes" type="java.util.List<com.coretex.data.MetaTypeDTO>"--%>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:abstractPage pageTitle="Type manager">
    <jsp:attribute name="footerJs">

        <script src="<c:url value="/resources/bootstrap/js/items.js" />"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="items">
            <div class="row">
                <div class="col">Create item</div>
            </div>
            <div class="row">
                <div class="col-2">Item Type: </div>
                <div class="col-4"></div>
            </div>
        </div>
    </jsp:body>
</template:abstractPage>