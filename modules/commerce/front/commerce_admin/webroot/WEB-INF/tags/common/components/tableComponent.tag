<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="tableId" required="true" rtexprvalue="true" %>
<%@ attribute name="dataSourceLink" required="true" rtexprvalue="true" %>
<%@ attribute name="rowId" required="true" %>

<%@ attribute name="description" required="false" %>
<%@ attribute name="actionTarget" required="false" %>
<%@ attribute name="deleteActionPath" required="false" %>
<%@ attribute name="actionPath" required="false" %>

<%@ attribute name="fullCardButton" required="false" type="java.lang.Boolean" %>
<%@ attribute name="minimizeCardButton" required="false" type="java.lang.Boolean" %>
<%@ attribute name="ordering" required="false" type="java.lang.Boolean" %>
<%@ attribute name="searching" required="false" type="java.lang.Boolean" %>

<%@ attribute name="cardHeader" required="false" fragment="true" %>
<%@ attribute name="theader" required="true" fragment="true" %>
<%@ attribute name="columns" required="true" fragment="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="common-components" tagdir="/WEB-INF/tags/common/components" %>

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
        <common-components:cardBlock title="${title}"
                                     description="${description}"
                                     minimizeCardButton="${fullCardButton}"
                                     fullCardButton="${fullCardButton}">
                <jsp:attribute name="cardHeader">
                    <jsp:invoke fragment="cardHeader"/>
                </jsp:attribute>
                <jsp:attribute name="cardBlock">
                    <script>
                        $(document).ready(function () {
                            $('#${tableId}').DataTable({
                                "responsive": true,
                                "ordering": ${ordering},
                                "searching": ${searching},
                                "processing": true,
                                "serverSide": true,
                                "ajax": {
                                    "url": "<c:url value="${dataSourceLink}"/>",
                                    "type": "POST"
                                },
                                "rowId": '${rowId}',
                                "columns": <jsp:invoke fragment="columns"/>
                                <c:if test="${not empty actionTarget}">,
                                "columnDefs": [{
                                    "targets": ${actionTarget},
                                    // "data": null,
                                    "searchable": false,
                                    'render': function (data, type, row) {
                                        return `<div class="tabledit-toolbar btn-toolbar" style="text-align: left;">
                                        <div class="btn-group btn-group-sm" style="float: none;">
                                            <button type="button" data-item-uuid="\${row['${rowId}']}"
                                                    onclick="edit_${tableId}(this)"
                                                    class="edit-${tableId}-button tabledit-edit-button btn btn-primary waves-effect waves-light"
                                                    style="float: none;margin: 5px;">
                                                <span class="icofont icofont-ui-edit"></span>
                                            </button>
                                        </div>
                                        <c:if test="${not empty deleteActionPath}">
                                            <div class="btn-group btn-group-sm" style="float: none;">
                                                <button type="button"
                                                        onclick="removeItemModalPanel('${pageContext.request.contextPath}${deleteActionPath}/\${row['${rowId}']}')"
                                                        class="tabledit-delete-button btn btn-danger waves-effect waves-light removeItem"
                                                        style="float: none;margin: 5px;">
                                                    <span class="icofont icofont-close"></span>
                                                </button>
                                            </div>
                                        </c:if>
                                    </div>`
                                    }
                                }]
                                </c:if>
                            });

                        });
                        <c:if test="${not empty actionPath}">
                            function edit_${tableId}(target) {
                                window.location.pathname = "${pageContext.request.contextPath}${actionPath}/" + target.dataset.itemUuid;
                                console.log("Edit: "+target.dataset.itemUuid);
                            }
                        </c:if>

                    </script>

                <div class="table-responsive dt-responsive">
                    <table class="table table-striped table-bordered" id="${tableId}">
                        <thead>
                        <jsp:invoke fragment="theader"/>
                        </thead>
                    </table>
                </div>
                </jsp:attribute>

    </common-components:cardBlock>

    </div>
</div>