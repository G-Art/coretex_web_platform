<%--@elvariable id="store" type="com.coretex.commerce.data.StoreData"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags-account" tagdir="/WEB-INF/tags/account" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>

<div class="row">
    <div class="col-lg-12">
        <c:url var="saveUrl" value="/store/save"/>

        <form id="productForm" action="${saveUrl}" method="post"
              class="md-float-material card-block">
            <input type="hidden" name="uuid" value="${store.uuid}">
            <components:cardBlock title="Store"
                                  description="${store.uuid == null ? 'Create' : 'Edit - '.concat(store.code)}">
                <jsp:attribute name="cardHeader">
                    <c:if test="${store != null}">
                        <a href="<c:url value="/store"/>"
                           class="btn btn-primary f-left d-inline-block">
                            <i class="icofont icofont-arrow-left m-r-5"></i> Back
                        </a>
                    </c:if>
                    <button type="button" data-request-url="<c:url value="/store/remove/${store.uuid}"/>"
                            class="btn btn-danger f-right d-inline-block removeItem">
                        <i class="icofont icofont-close m-r-5"></i> Remove
                    </button>
                </jsp:attribute>
                <jsp:attribute name="cardBlock">

                        <tags-account:accountTabsComonent>
                            <jsp:attribute name="tabs">
                                <tags-account:tab tabId="edit" tabName="Store Edit" active="true"/>
                            </jsp:attribute>
                            <jsp:attribute name="tabsBody">
                                <tags-account:tabContent tabId="edit" active="true">
                                    <jsp:include page="editStoreTab.jsp"/>
                                </tags-account:tabContent>
                            </jsp:attribute>
                        </tags-account:accountTabsComonent>

                        <div class="row">
                            <div class="col-sm-12">
                                <div class="text-center m-t-20">
                                    <button type="submit"
                                            class="btn btn-primary waves-effect waves-light m-r-10">
                                        Save
                                    </button>
                                </div>
                            </div>
                        </div>
                    </jsp:attribute>
            </components:cardBlock>
        </form>
        <div class="md-overlay"></div>
    </div>
</div>