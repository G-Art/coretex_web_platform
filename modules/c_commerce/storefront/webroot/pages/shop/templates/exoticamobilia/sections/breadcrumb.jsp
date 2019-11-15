
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>


<div class="page-intro d-flex align-items-stretch" style="margin-top: 0px;">
    <div class="container d-flex align-items-stretch">
        <div class="row ">
            <div class="col-md-12">
                <div class="cont_principal d-flex align-items-stretch">
                    <div class="cont_breadcrumbs d-flex align-items-stretch">
                        <div class="cont_breadcrumbs_1 d-flex align-items-stretch">
                            <ul class="d-flex align-items-stretch m-0">
                                <c:forEach items="${requestScope.BREADCRUMB.breadCrumbs}" var="breadcrumb" varStatus="count">
                                    <li class="active p-2 d-flex align-items-center"><c:if test="${count.index==0}"><i class="fa fa-home pr-10 float-left"></i></c:if>
                                        <a href="<c:url value="${breadcrumb.url}" /><sm:breadcrumbParam/>">${breadcrumb.label}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



