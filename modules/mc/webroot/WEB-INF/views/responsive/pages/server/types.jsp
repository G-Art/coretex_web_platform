<%--@elvariable id="mataTypes" type="java.util.List<com.coretex.data.MetaTypeDTO>"--%>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:abstractPage pageTitle="Type manager">
    <jsp:attribute name="footerJs">

    </jsp:attribute>
    <jsp:body>

        <div class="row">
            <div class="col accordion tableText" id="accordionType">
                <c:forEach var="metaType" items="${mataTypes}" varStatus="status">
                    <div class="card">
                        <div class="card-header" id="heading${status.index}">
                            <div class="row btn-link collapsed" data-toggle="collapse"
                                 data-target="#collapse${status.index}"
                                 aria-expanded="true" aria-controls="collapseOne">
                                <div class="col-4">${metaType.typeCode}</div>
                                <div class="col">${metaType.description}</div>
                            </div>
                        </div>

                        <div id="collapse${status.index}" class="collapse" aria-labelledby="heading${status.index}"
                             data-parent="#accordionType">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-1">Type Code:</div>
                                    <div class="col-4">${metaType.typeCode}</div>
                                    <div class="col-1">UUID:</div>
                                    <div class="col-4">${metaType.uuid}</div>
                                </div>
                                <div class="row">
                                    <div class="col-1">Item Class:</div>
                                    <div class="col-4">${metaType.itemClass}</div>
                                    <div class="col-1">Create date:</div>
                                    <div class="col-4">${metaType.createDate}</div>
                                </div>
                                <div class="row">
                                    <div class="col-1">Meta Type:</div>
                                    <div class="col-4">${metaType.metaType}</div>
                                    <div class="col-1">Update date:</div>
                                    <div class="col-4">${metaType.updateDate}</div>
                                </div>
                                <div class="row">
                                    <div class="col-1">Table Name:</div>
                                    <div class="col-4">${metaType.tableName}</div>
                                </div>
                                <div class="row">
                                    <div class="col-1">Table Owner:</div>
                                    <div class="col-4"><span
                                            class="badge badge-pill badge-info">${metaType.tableOwner}</span></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </jsp:body>
</template:abstractPage>