<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="tableId" required="true" rtexprvalue="true" %>
<%@ attribute name="dataSourceLink" required="true" rtexprvalue="true" %>

<%@ attribute name="description" required="false" %>
<%@ attribute name="rowId" required="false" %>
<%@ attribute name="actionTarget" required="false" %>


<%@ attribute name="fullCardButton" required="false" type="java.lang.Boolean" %>
<%@ attribute name="minimizeCardButton" required="false" type="java.lang.Boolean" %>
<%@ attribute name="ordering" required="false" type="java.lang.Boolean" %>
<%@ attribute name="searching" required="false" type="java.lang.Boolean" %>

<%@ attribute name="theader" required="true" fragment="true" %>
<%@ attribute name="columns" required="true" fragment="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${empty fullCardButton}">
    <c:set var="fullCardButton" value="true"/>
</c:if>
<c:if test="${empty minimizeCardButton}">
    <c:set var="minimizeCardButton" value="true"/>
</c:if>
<c:if test="${empty ordering}">
    <c:set var="ordering" value="false"/>
</c:if>

<c:if test="${empty searching}">
    <c:set var="searching" value="false"/>
</c:if>


<div class="row">
    <div class="col-sm-12">
        <div class="card">
            <div class="card-header">
                <h5>${title}</h5>
                <span>${description}</span>
                <c:if test="${fullCardButton || minimizeCardButton}">
                    <div class="card-header-right">
                        <ul class="list-unstyled card-option">
                            <c:if test="${fullCardButton}">
                                <li><i class="feather icon-maximize full-card"></i></li>
                            </c:if>
                            <c:if test="${minimizeCardButton}">
                                <li><i class="feather icon-minus minimize-card"></i></li>
                            </c:if>
                                <%--                                    <li><i class="feather icon-trash-2 close-card"></i></li>--%>
                        </ul>
                    </div>
                </c:if>
            </div>

            <div class="card-block">
                <script>
                    $(document).ready(function () {
                        $('#${tableId}').DataTable({
                            "ordering": ${ordering},
                            "searching": ${searching},
                            "processing": true,
                            "serverSide": true,
                            "ajax": "<c:url value="${dataSourceLink}"/>",
                            <c:if test="${not empty rowId}">"rowId": '${rowId}',</c:if>
                            "columns": <jsp:invoke fragment="columns"/>
                            <c:if test="${not empty actionTarget}">,
                            "columnDefs": [{
                                "targets": ${actionTarget},
                                "data": null,
                                "searchable": false,
                                "defaultContent": `<div class="tabledit-toolbar btn-toolbar" style="text-align: left;">
                                                                    <div class="btn-group btn-group-sm" style="float: none;">
                                                                        <button type="button"
                                                                                 class="tabledit-edit-button btn btn-primary waves-effect waves-light"
                                                                                 style="float: none;margin: 5px;">
                                                                            <span class="icofont icofont-ui-edit"></span>
                                                                        </button>
                                                                    </div>
                                                                </div>`
                            }]
                            </c:if>
                        });
                    });
                </script>

                <div class="table-responsive dt-responsive">
                    <table class="table table-striped table-bordered" id="${tableId}">
                        <thead>
                            <jsp:invoke fragment="theader"/>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>