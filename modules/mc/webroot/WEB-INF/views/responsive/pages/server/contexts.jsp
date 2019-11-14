<%--@elvariable id="allWebContexts" type="java.util.Map<java.lang.String,java.lang.String>"--%>

<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:abstractPage pageTitle="Context manager">
    <jsp:attribute name="footerJs">
<%--        <script src="https://unpkg.com/vue-chartjs/dist/vue-chartjs.min.js"></script>--%>
        <script src="<c:url value="/resources/bootstrap/js/components/memorychart.com.js" />"></script>
        <script src="<c:url value="/resources/bootstrap/js/chart.js" />"></script>
    </jsp:attribute>
    <jsp:body>

        <memory-chart title="Memory usage"></memory-chart>

        <div class="card mb-3">
            <div class="card-header">System Info</div>
            <div class="card-body ">
                <div class="list-group tableText">
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">OS name:</div>
                            <div class="col-auto">${systemInfo.nameOS}</div>
                        </div>
                    </div>
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">OS Type:</div>
                            <div class="col-md-auto">${systemInfo.osType}</div>
                        </div>
                    </div>
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">OS Version:</div>
                            <div class="col-md-auto">${systemInfo.osVersion}</div>
                        </div>
                    </div>
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">Processor Identifier:</div>
                            <div class="col-md-auto">${systemInfo.processorIdentifier}</div>
                        </div>
                    </div>
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">Processor Architecture:</div>
                            <div class="col-md-auto">${systemInfo.processorArchitecture}</div>
                        </div>
                    </div>
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">Processor Architew6432:</div>
                            <div class="col-md-auto">${systemInfo.processorArchitew6432}</div>
                        </div>
                    </div>
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">Number Of Processors:</div>
                            <div class="col-md-auto">${systemInfo.numberOfProcessors}</div>
                        </div>
                    </div>
                    <div class="row list-group-item list-group-item-action">
                        <div class="row">
                            <div class="col-3">Available Processors:</div>
                            <div class="col-md-auto">${systemInfo.availableProcessors}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card mb-3">
            <div class="card-header">Available contexts</div>
            <div class="card-body">
                <div class="list-group tableText">
                    <c:forEach var="context" items="${allWebContexts}">
                        <c:url var="reload" value="/reload?context=${context.key}"/>
                        <div class="row list-group-item list-group-item-action">
                            <div class="row">
                                <div class="col-md-3">Context path: ${context.key}</div>
                                <div class="col-md">URL: <a href="${context.value}">${context.value}</a></div>
                                <div class="col-md-1">
                                    <a href="${reload}" class="btn btn-warning btn-sm">Reload</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

    </jsp:body>

</template:abstractPage>