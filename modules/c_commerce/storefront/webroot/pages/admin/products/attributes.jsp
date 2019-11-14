<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>


<div class="tabbable">

    <jsp:include page="/common/adminTabs.jsp"/>

    <div class="tab-content">

        <div class="tab-pane active" id="catalogue-section">


            <div class="sm-ui-component">


                <c:if test="${product.uuid!=null }">
                    <c:set value="${product.uuid}" var="productId" scope="request"/>
                    <jsp:include page="/pages/admin/products/product-menu.jsp"/>
                </c:if>

                <h3><s:message code="label.product.attributes" text="Product attributes"/></h3>
                <br/>
                <strong><c:out value="${product.sku}"/></strong>

                <br/>
                <a href="<c:url value="/admin/products/attribute/createAttribute.html?productId=${product.uuid}"/>"><s:message
                        code="label.product.attribute.create" text="Create product attribute"/></a>
                <br/><br/>

                <!-- Listing grid include -->
                <c:set value="/admin/products/attributes/page.html?productId=${product.uuid}" var="pagingUrl"
                       scope="request"/>
                <c:set value="/admin/attributes/attribute/remove.html" var="removeUrl" scope="request"/>
                <c:set value="/admin/products/attributes/editAttribute.html" var="editUrl" scope="request"/>
                <c:set value="/admin/products/attributes/list.html?id=${product.uuid}" var="afterRemoveUrl"
                       scope="request"/>
                <c:set var="entityId" value="attributeId" scope="request"/>
                <c:set var="appendQueryStringToEdit" value="productId=${product.uuid}" scope="request"/>
                <c:set var="groupByEntity" value="attribute" scope="request"/>
                <c:set var="componentTitleKey" value="label.product.attributes" scope="request"/>
                <c:set var="gridHeader" value="/pages/admin/products/attributes-gridHeader.jsp" scope="request"/>
                <c:set var="canRemoveEntry" value="true" scope="request"/>

                <jsp:include page="/pages/admin/components/list.jsp"></jsp:include>
                <!-- End listing grid include -->


            </div>


        </div>


    </div>

</div>