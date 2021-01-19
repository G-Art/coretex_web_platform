<%--@elvariable id="mataTypes" type="java.util.List<com.coretex.data.MetaTypeDTO>"--%>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:abstractPage pageTitle="Type manager">
    <jsp:attribute name="footerJs">

        <script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.4.2/ace.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.4.2/ext-language_tools.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.4.2/theme-merbivore_soft.js"></script>
        <script src="<c:url value="/resources/bootstrap/js/components/queryresultviewer.com.js" />"></script>
        <script src="<c:url value="/resources/bootstrap/js/components/stringresultviewer.com.js" />"></script>
        <script src="<c:url value="/resources/bootstrap/js/components/mapresultviewer.com.js" />"></script>
        <script src="<c:url value="/resources/bootstrap/js/query.js" />"></script>
    </jsp:attribute>
    <jsp:body>


        <div class="panel panel-default">
            <div class="panel-body">
                <div id="query" style="height:300px"></div>
            </div>
        </div>

        <div id="queryExecutor">
            <div class="row">
                <div class="col-2">
                    <label>Count</label> <input v-model="count" id="count" type="text"/>
                </div>
                <div class="col-2">
                    <label>Page</label> <input v-model="page" id="page" type="text"/>
                </div>
            </div>
            <div class="row">
                <div class="col-1">
                    <span id="execute" href="#" class="btn btn-warning btn-sm" v-on:click="execute">Execute</span>
                </div>
            </div>
            <query-result-viewer v-bind:result="queryResult"></query-result-viewer>
        </div>
    </jsp:body>
</template:abstractPage>