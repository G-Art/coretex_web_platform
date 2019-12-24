<%--@elvariable id="currentUser" type="com.coretex.commerce.data.UserData"--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/template" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content"%>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<template:adminPage pageTitle="Category hierarchy">
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
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/bootstrap-tagsinput/css/bootstrap-tagsinput.css"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bower_components/jstree/css/style.min.css"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/pages/treeview/treeview.css"/>">
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
                src="<c:url value="/resources/bower_components/jstree/js/jstree.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/js/script.js"/>"></script>
	</jsp:attribute>
    <jsp:body>
        <!-- Page-header start -->
        <div class="page-header">
            <div class="row align-items-end">
                <div class="col-lg-8">
                    <div class="page-header-title">
                        <div class="d-inline">
                            <h4>Category hierarchy</h4>
                            <span>Category hierarchy tree</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-header end -->

        <!-- Page-body start -->
        <div class="page-body">
            <content:contentInfoBlocks/>

            <div class="row">
                <div class="col-sm-12 col-lg-12">
                    <div class="card">
                        <div class="card-header">
                            <h5>Category hierarchy</h5>
                        </div>
                        <div class="card-block">
                            <div class="card-block tree-view">
                                <script>
                                    $(document).ready(function (){
                                        // Drag & Drop
                                        $('#categoriesTree').jstree({
                                            'core' : {
                                                'check_callback' : true,
                                                'themes' : {
                                                    'responsive': true,
                                                    'dots' : true
                                                },
                                                'data' : {
                                                    'url' : function (node) {
                                                        if (node == -1){
                                                            return '${pageContext.request.contextPath}/category/h/';
                                                        }
                                                        return '${pageContext.request.contextPath}/category/h/'+node.id;
                                                    }
                                                }
                                            },
                                            "types" : {
                                                'default' : {
                                                    'icon' : 'icofont icofont-file-alt'
                                                },
                                                'active' : {
                                                    'icon' : 'icofont icofont-file-alt'
                                                },
                                                'notActive' : {
                                                    'icon' : 'icofont icofont-file'
                                                }

                                            },
                                            "plugins" : [ "dnd", "types", "wholerow"]
                                        }).on('move_node.jstree', function (e, data) {

                                            if(data.parent === "#"){
                                                $.post('${pageContext.request.contextPath}/category/parent/', { category: data.node.id})
                                                    .fail(function () {
                                                        data.instance.refresh();
                                                    })
                                            }else{
                                                $.post('${pageContext.request.contextPath}/category/parent/'+data.parent, { category: data.node.id})
                                                    .fail(function () {
                                                        data.instance.refresh();
                                                    })
                                            }
                                        });

                                    });
                                </script>
                                <div id="categoriesTree">
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Drag Tree card end -->
                </div>
            </div>

        </div>
        <!-- Page-body end -->

    </jsp:body>

</template:adminPage>