<%--@elvariable id="currentUser" type="com.coretex.commerce.data.UserData"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/template" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<template:adminPage pageTitle="Home">
    <jsp:attribute name="pageCss">
    <link rel="stylesheet" type="text/css"
          href="<c:url value="/resources/bower_components/datatables.net-bs4/css/dataTables.bootstrap4.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/icon/feather/css/feather.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/css/jquery.mCustomScrollbar.css"/>">
	</jsp:attribute>

    <jsp:attribute name="pageScripts">

    <script type="text/javascript"
            src="<c:url value="/resources/assets/js/jquery.mCustomScrollbar.concat.min.js"/>"></script>
    <script type="text/javascript"
            src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="<c:url value="/resources/bower_components/datatables.net-buttons/js/dataTables.buttons.min.js"/>"></script>

    <script type="text/javascript"
            src="<c:url value="/resources/bower_components/datatables.net-bs4/js/dataTables.bootstrap4.min.js"/>"></script>
    <script type="text/javascript"
            src="<c:url value="/resources/bower_components/datatables.net-responsive/js/dataTables.responsive.min.js"/>"></script>
    <script type="text/javascript"
            src="<c:url value="/resources/bower_components/datatables.net-responsive-bs4/js/responsive.bootstrap4.min.js"/>"></script>

    <script type="text/javascript" src="<c:url value="/resources/assets/js/script.js"/>"></script>

	</jsp:attribute>
    <jsp:body>

        <div class="page-body">
            <div class="row">
                <!-- task, page, download counter  start -->
                <div class="col-xl-4 col-md-8">
                    <div class="card bg-c-yellow update-card">
                        <div class="card-block">
                            <div class="row align-items-center">
                                <div class="col">
                                    <p class="m-b-5">Orders</p>
                                    <div class="row">
                                        <div class="col-sm-3 col-md-4"><h5 class="m-b-0">${orderCount}</h5></div>
                                        <div class="col-sm-4 col-md-5"><h5 class="m-b-0">$${orderTotal}</h5></div>
                                    </div>
                                </div>
                                <div class="col col-auto text-right">
                                    <i class="feather icon-shopping-cart f-50 text-c-yellow"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- task, page, download counter  end -->
            </div>
            <components:storeInfoComponent/>
            <components:tableComponent title="Recent Orders"
                                       description="List of recent orders"
                                       tableId="recentOrder"
                                       dataSourceLink="/order/paginated"
                                       rowId="uuid"
                                       actionTarget="4"
                                       actionPath="/order">
                <jsp:attribute name="theader">
                    <tr>
                        <th>Total</th>
                        <th>Store</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </jsp:attribute>
                <jsp:attribute name="columns">
                    [
                        {"data": "total"},
                        {"data": "store"},
                        {"data": "date"},
                        {"data": "status"}
                    ]
                </jsp:attribute>
            </components:tableComponent>
        </div>
    </jsp:body>

</template:adminPage>