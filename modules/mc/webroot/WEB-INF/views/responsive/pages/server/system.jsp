<%--@elvariable id="mataTypes" type="java.util.List<com.coretex.data.MetaTypeDTO>"--%>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/responsive/components" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:abstractPage pageTitle="System ">
    <jsp:attribute name="headerStyle">
        <style>
            #cy {
                position: relative;
                height: calc(100vh - 155px);
                width: 100%;
                margin: auto;
                background: #262626;
                background-image: repeating-linear-gradient(to bottom, #343434, #343434 1px, transparent 1px, transparent 18px),
                repeating-linear-gradient(to right, #343434, #343434 1px, transparent 1px, transparent 18px);
                /*background-image: radial-gradient(#343434 1px, transparent 0);*/
                /*background-size: 18px 18px;*/
            }

            .node {
                /*font-size: 4px;*/
                color: #ffffff;
                background:transparent;
            }

            .node-title {
                font-size: 8px;
                padding: 0 4px 0 4px;
                white-space: nowrap;
                text-overflow: ellipsis;
            }

            .node-body {
                font-size: 5px;
                white-space: nowrap;
                text-overflow: ellipsis;
            }

            .node-title.class{
                background: rgb(110,150,103);
                background: -moz-linear-gradient(90deg, rgba(110,150,103,1) 48%, rgba(0,0,0,0) 100%);
                background: -webkit-linear-gradient(90deg, rgba(110,150,103,1) 48%, rgba(0,0,0,0) 100%);
                background: linear-gradient(90deg, rgba(110,150,103,1) 48%, rgba(0,0,0,0) 100%);
                filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#6e9667",endColorstr="#000000",GradientType=1);
            }
            .node-title.abstract {
                background: rgb(150, 103, 103);
                background: -moz-linear-gradient(90deg, rgba(150, 103, 103,1) 48%, rgba(0,0,0,0) 100%);
                background: -webkit-linear-gradient(90deg, rgba(150, 103, 103,1) 48%, rgba(0,0,0,0) 100%);
                background: linear-gradient(90deg, rgba(150, 103, 103,1) 48%, rgba(0,0,0,0) 100%);
                filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#966767",endColorstr="#000000",GradientType=1);
            }

        </style>
        <link href="<c:url value="/resources/vendor/cytoscape/css/cytoscape.js-navigator.css"/>" rel="stylesheet"
              type="text/css">
    </jsp:attribute>
    <jsp:attribute name="footerJs">
        <script src="<c:url value="/resources/vendor/cytoscape/js/cytoscape.esm.min.js" />"></script>
        <script src="<c:url value="/resources/vendor/cytoscape/js/dagre.min.js" />"></script>
        <script src="<c:url value="/resources/vendor/cytoscape/js/cytoscape-dagre.min.js" />"></script>
        <script src="<c:url value="/resources/vendor/cytoscape/js/cytoscape-navigator.min.js" />"></script>
        <script src="<c:url value="/resources/vendor/cytoscape/js/cytoscape-node-html.min.js" />"></script>
        <script src="<c:url value="/resources/bootstrap/js/system.js" />"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="system">
            <div class="row">
                <select v-model="selected" name="rankDirection" id="rankDirection" v-on:change="onChange($event)">
                    <option value="TB" selected>Top to Bottom</option>
                    <option value="LR">Left to Right</option>
                </select>
            </div>
            <div class="row">
                <div id="cy"></div>
            </div>
        </div>
    </jsp:body>
</template:abstractPage>