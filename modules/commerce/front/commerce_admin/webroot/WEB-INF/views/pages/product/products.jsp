<%--@elvariable id="currentUser" type="com.coretex.commerce.data.UserData"--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/template" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<template:adminPage pageTitle="Products">
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
	    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/sweetalert/css/sweetalert.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/css/component.css"/>"/>
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
        <script type="text/javascript"
                src="<c:url value="/resources/assets/js/modalEffects.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/sweetalert/js/sweetalert.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/js/classie.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/js/script.js"/>"></script>
        <script>
            'use strict';
            $(document).ready(function() {
                $(".close_btn").on("click", function() {
                    $('.pname').val('');
                    $('.jFiler-items').css('display','none');
                    $('.stock').val('');
                    $('.pamount').val('');
                });
            } );
        </script>

	</jsp:attribute>
    <jsp:body>
        <!-- Page-header start -->
        <div class="page-header">

            <div class="row align-items-end">
                <div class="col-lg-8">
                    <div class="page-header-title">
                        <div class="d-inline">
                            <h4>Products</h4>
                            <span>Products information</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Page-header end -->

        <!-- Page-body start -->
        <div class="page-body">
            <content:contentInfoBlocks/>
            <components:tableComponent title="All products"
                                       description="List of products"
                                       tableId="productsTable"
                                       dataSourceLink="/product/paginated"
                                       rowId="uuid"
                                       actionTarget="5"
                                       actionPath="/product"
                                       deleteActionPath="/product/remove">
                <jsp:attribute name="cardHeader">
                    <button type="button" class="btn btn-primary waves-effect waves-light f-right d-inline-block md-trigger"
                            data-modal="modal-13">
                        <i class="icofont icofont-plus m-r-5"></i> Add Product
                    </button>
                </jsp:attribute>
                <jsp:attribute name="theader">
                    <tr>
                        <th>Image</th>
                        <th>SKU</th>
                        <th>Name</th>
                        <th>Available</th>
                        <th>Store</th>
                        <th>Action</th>
                    </tr>
                </jsp:attribute>
                <jsp:attribute name="columns">
                    [
                        {"data": "image",
                         "render": function (data, type, row) {
                                if(data){
                                    return  `<img style="max-width: 200px;" src="/v1\${data}" class="img-fluid">`
                                }else {
                                    return  `<img style="max-width: 200px;" src="<c:url value="/resources/assets/images/service/no_image.svg"/>" class="img-fluid">`
                                }
                            }
                        },
                        {"data": "code"},
                        {"data": "name"},
                        {"data": "available"},
                        {"data": "store"}
                    ]
                </jsp:attribute>
            </components:tableComponent>

            <div class="md-modal md-effect-13 addcontact" id="modal-13">
                <div class="md-content">
                    <h3 class="f-26">Add Product</h3>
                    <div>
                        <form action="<c:url value="/product/new"/>" method="get">

                            <div class="input-group">
                                <span class="input-group-addon"><i class="icofont icofont-user"></i></span>
                                <input type="text" name="code" class="form-control pname" placeholder="Code">
                            </div>
                            <div class="text-center">
                                <button type="submit" class="btn btn-primary waves-effect m-r-20 f-w-600 d-inline-block save_btn">Create</button>
                                <button type="button" class="btn btn-primary waves-effect m-r-20 f-w-600 md-close d-inline-block close_btn">Close</button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
            <div class="md-overlay"></div>
        </div>
        <!-- Page-body end -->

    </jsp:body>

</template:adminPage>