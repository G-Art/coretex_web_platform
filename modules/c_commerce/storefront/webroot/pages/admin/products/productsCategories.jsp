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


                <c:if test="${product.uuid!=null}">
                    <c:set value="${product.uuid}" var="productId" scope="request"/>
                    <jsp:include page="/pages/admin/products/product-menu.jsp"/>
                </c:if>


                <h3>
                    <s:message code="label.product.category.association" text="Associate to categories"/>
                </h3>


                <c:url var="addCategory" value="/admin/products/addProductToCategories.html"/>
                <form:form method="POST" modelAttribute="product" action="${addCategory}">
                    <form:errors path="*" cssClass="alert alert-error" element="div"/>
                    <div id="store.success" class="alert alert-success" style="
                    <c:choose>
                    <c:when test="${success!=null}">display:block;</c:when>
                        <c:otherwise>display:none;</c:otherwise></c:choose>">
                        <s:message code="message.success" text="Request successfull"/>
                    </div>
                    <br/>
                    <strong><c:out value="${product.sku}"/></strong>
                    <br/><br/>

                    <div class="control-group">
                        <label><s:message code="label.productedit.categoryname" text="Category"/></label>
                        <div class="controls">
                            <select id="uuid" name="uuid">
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.uuid}" ${category.uuid eq categoryId ? 'selected' : ''}>${category.name}</option>
                                </c:forEach>
                            </select>

                        </div>
                    </div>


                    <input type="hidden" name="productId" value="${product.uuid}">
                    <div class="form-actions">
                        <div class="pull-right">
                            <button type="submit" class="btn btn-success"><s:message code="label.generic.add"
                                                                                     text="Add"/></button>
                        </div>
                    </div>

                </form:form>


                <br/>
                <!-- Listing grid include -->
                <c:set value="/admin/product-categories/paging.html?productId=${product.uuid}" var="pagingUrl"
                       scope="request"/>
                <c:set value="/admin/product-categories/remove.html?productId=${product.uuid}" var="removeUrl"
                       scope="request"/>
                <c:set value="/admin/products/displayProductToCategories.html?id=${product.uuid}" var="refreshUrl"
                       scope="request"/>
                <c:set var="entityId" value="categoryId" scope="request"/>
                <c:set var="componentTitleKey" value="label.categories.title" scope="request"/>
                <c:set var="canRemoveEntry" value="true" scope="request"/>
                <c:set var="gridHeader" value="/pages/admin/products/product-categories-gridHeader.jsp"
                       scope="request"/>
<%--                <jsp:include page="/pages/admin/components/list.jsp"></jsp:include>--%>
                <!-- End listing grid include -->


            </div>
        </div>
    </div>
</div>	