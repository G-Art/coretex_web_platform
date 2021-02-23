<%--@elvariable id="ds" type="com.coretex.commerce.data.DeliveryServiceData"--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/template" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<template:adminPage pageTitle="Delivery service">
    <jsp:attribute name="pageCss">
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/assets/pages/advance-elements/css/bootstrap-datetimepicker.css"/>"/>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/bootstrap-daterangepicker/css/daterangepicker.css"/>"/>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/datedropper/css/datedropper.min.css"/>"/>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/assets/pages/data-table/css/buttons.dataTables.min.css"/>"/>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/datatables.net-responsive-bs4/css/responsive.bootstrap4.min.css"/>"/>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/datatables.net-bs4/css/dataTables.bootstrap4.min.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/icon/feather/css/feather.css"/>"/>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/assets/css/jquery.mCustomScrollbar.css"/>">
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/select2/css/select2.min.css"/>">
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/switchery/css/switchery.min.css"/>">
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/bootstrap-tagsinput/css/bootstrap-tagsinput.css"/>">
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/bower_components/sweetalert/css/sweetalert.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/assets/css/component.css"/>"/>
	</jsp:attribute>

    <jsp:attribute name="pageScripts">
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
                src="<c:url value="/resources/bower_components/switchery/js/switchery.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/bootstrap-tagsinput/js/bootstrap-tagsinput.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/js/modalEffects.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/bower_components/sweetalert/js/sweetalert.min.js"/>"></script>
        <script type="text/javascript"
                src="<c:url value="/resources/assets/js/classie.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/js/script.js"/>"></script>
        <%--        <script src="https://cdn.ckeditor.com/ckeditor5/17.0.0/inline/ckeditor.js"></script>--%>
        <%--        <script src="<c:url value="/resources/assets/pages/ckeditor/ckeditor.js"/>"></script>--%>
        <script>
            'use strict';
            $(document).ready(function () {
                $(".close_btn").on("click", function () {
                    $('.pname').val('');
                    $('.jFiler-items').css('display', 'none');
                    $('.stock').val('');
                    $('.pamount').val('');
                });
            });
        </script>
         <script>
             let elemprimary = document.querySelector('.js-success');
             let switchery = new Switchery(elemprimary, {
                 color: '#93BE52',
                 secondaryColor: '#FC4050',
                 jackColor: '#fff'
             });

         </script>
	</jsp:attribute>
    <jsp:body>
        <!-- Page-header start -->
        <div class="page-header">
            <div class="row align-items-end">
                <div class="col-lg-8">
                    <div class="page-header-title">
                        <div class="d-inline">
                            <h4>Delivery service</h4>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="page-body">
            <div class="row">
                <div class="col-lg-12">
                    <form id="deliveryServiceForm"
                          action="
                          <c:url var="saveUrl" value="/delivery/service/${ds.uuid}/save"/>" method="post"
                          class="md-float-material card-block">
                        <input type="hidden" name="uuid" value="${ds.uuid}">
                        <input type="hidden" name="type" value="${ds.type}"/>
                        <components:cardBlock title="Delivery service: ${ds.code} "
                                              description="${ds.name['en']}">
                            <jsp:attribute name="cardBlock">
                                <components:cardBlock title="Essential data">
                                    <jsp:attribute name="cardBlock">
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <form-tags:multiLanguageInput
                                                        placeholder="Name"
                                                        name="name"
                                                        icon="icofont icofont-copy-alt"
                                                        dataMap="${ds.name}">

                                                </form-tags:multiLanguageInput>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="input-group">
                                                    <input type="checkbox" class="js-success"
                                                           name="active" ${ds.active ? 'checked' : ''}
                                                           placeholder="Active">
                                                </div>
                                            </div>
                                        </div>
                                    </jsp:attribute>
                                </components:cardBlock>
                                <components:cardBlock title="Additianal info">
                                    <jsp:attribute name="cardBlock">
                                        <c:set var="deliveryServiceCode" value="${ds.code}"/>
                                        <jsp:include page="/fragment/delivery/${deliveryServiceCode}/deliveryServiceInfo.jsp"/>
                                    </jsp:attribute>
                                </components:cardBlock>
                            </jsp:attribute>

                        </components:cardBlock>
                    </form>
                </div>
            </div>

        </div>

    </jsp:body>

</template:adminPage>