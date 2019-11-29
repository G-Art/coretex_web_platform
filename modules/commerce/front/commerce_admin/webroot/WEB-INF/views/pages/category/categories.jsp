<%--@elvariable id="currentUser" type="com.coretex.commerce.admin.data.UserData"--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/template" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<template:adminPage pageTitle="Categories">
    <jsp:attribute name="pageCss">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/pages/advance-elements/css/bootstrap-datetimepicker.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap-daterangepicker/css/daterangepicker.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/datedropper/css/datedropper.min.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/pages/data-table/css/buttons.dataTables.min.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/datatables.net-responsive-bs4/css/responsive.bootstrap4.min.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/datatables.net-bs4/css/dataTables.bootstrap4.min.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/icon/feather/css/feather.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/css/jquery.mCustomScrollbar.css"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/select2/css/select2.min.css"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/switchery/css/switchery.min.css"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap-tagsinput/css/bootstrap-tagsinput.css"/>">
	</jsp:attribute>

    <jsp:attribute name="pageScripts">
        <script>
            var CKEDITOR_BASEPATH = '<c:url value="/resources/assets/pages/ckeditor/"/>';
        </script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/pages/advance-elements/moment-with-locales.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/bootstrap-datepicker/js/bootstrap-datepicker.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/pages/advance-elements/bootstrap-datetimepicker.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/bootstrap-daterangepicker/js/daterangepicker.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/datedropper/js/datedropper.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/datatables.net/js/jquery.dataTables.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/datatables.net-bs4/js/dataTables.bootstrap4.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/datatables.net-responsive/js/dataTables.responsive.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/datatables.net-responsive-bs4/js/responsive.bootstrap4.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/pages/ckeditor/ckeditor.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/pages/chart/echarts/js/echarts-all.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/js/jquery.mCustomScrollbar.concat.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/pages/form-validation/validate.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/select2/js/select2.full.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/bootstrap-tagsinput/js/bootstrap-tagsinput.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/js/script.js"/>"></script>
	</jsp:attribute>
    <jsp:body>
        <!-- Page-header start -->
        <div class="page-header">
            <div class="row align-items-end">
                <div class="col-lg-8">
                    <div class="page-header-title">
                        <div class="d-inline">
                            <h4>Categories</h4>
                            <span>Common categories information</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-header end -->

        <!-- Page-body start -->
        <div class="page-body">
            <div class="row">
                <!-- task, page, download counter  start -->
                <div class="col-xl-4 col-md-8">
                    <div class="card bg-c-blue update-card">
                        <div class="card-block">
                            <div class="row align-items-center">
                                <div class="col">
                                    <p class="m-b-5">Categorise</p>
                                    <div class="row">
                                        <div class="col-sm-3 col-md-4"><h5 class="m-b-0">${categoriesCount}</h5></div>
                                    </div>
                                </div>
                                <div class="col col-auto text-right">
                                    <i class="icofont icofont-chart-flow-alt-2 f-50 text-c-blue"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- task, page, download counter  end -->
            </div>

            <components:tableComponent title="All categories"
                                       description="List of categories"
                                       tableId="categoryTable"
                                       dataSourceLink="/category/paginated"
                                       rowId="uuid"
                                       actionTarget="7" >
                <jsp:attribute name="theader">
                    <tr>
                        <th>Code</th>
                        <th>Name</th>
                        <th>Store</th>
                        <th>Parent</th>
                        <th>Visible</th>
                        <th>Products count</th>
                        <th>Subcategories count</th>
                        <th>Action</th>
                    </tr>
                </jsp:attribute>
                <jsp:attribute name="columns">
                    [
                        {"data": "code"},
                        {"data": "name"},
                        {"data": "merchantStore"},
                        {"data": "parent.name"},
                        {"data": "visible"},
                        {"data": "productCount"},
                        {"data": "subCategoriesCount"}
                    ]
                </jsp:attribute>
            </components:tableComponent>

        </div>
        <!-- Page-body end -->

    </jsp:body>

</template:adminPage>