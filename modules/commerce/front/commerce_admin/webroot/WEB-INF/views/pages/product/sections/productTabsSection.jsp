<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%--@elvariable id="stores" type="java.util.List<com.coretex.commerce.data.StoreData>"--%>
<%--@elvariable id="baseProduct" type="com.coretex.items.cx_core.ProductItem"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags-account" tagdir="/WEB-INF/tags/account" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>

<div class="row">
    <div class="col-lg-12">
        <c:choose>
            <c:when test="${not empty baseProduct}">
                <c:url var="saveUrl" value="/product/${baseProduct.uuid}/variant/save"/>
            </c:when>
            <c:otherwise>
                <c:url var="saveUrl" value="/product/save"/>
            </c:otherwise>
        </c:choose>

        <form id="productForm" action="${saveUrl}" method="post"
              class="md-float-material card-block">
            <c:if test="${not empty baseProduct}">
                <input type="hidden" name="variantType" value="${product.variantType}">
            </c:if>
            <input type="hidden" name="uuid" value="${product.uuid}">
            <components:cardBlock title="Product"
                                  description="${product.uuid == null ? 'Create' : 'Edit - '.concat(product.code)}">
                <jsp:attribute name="cardHeader">
                    <c:if test="${baseProduct != null}">
                        <c:choose>
                            <c:when test="${baseProduct.metaType.typeCode eq 'Product'}">
                                <c:url var="back" value="/product/${baseProduct.uuid}"/>
                            </c:when>
                            <c:otherwise>
                                <c:url var="back"
                                       value="/product/${baseProduct.baseProduct.uuid}/variant/${baseProduct.uuid}"/>
                            </c:otherwise>
                        </c:choose>
                        <a href="${back}"
                           class="btn btn-primary f-left d-inline-block">
                            <i class="icofont icofont-arrow-left m-r-5"></i> Back
                        </a>
                    </c:if>
                    <button type="button" data-request-url="<c:url value="/product/remove/${product.uuid}"/>"
                            class="btn btn-danger f-right d-inline-block removeItem">
                        <i class="icofont icofont-close m-r-5"></i> Remove
                    </button>
                </jsp:attribute>
                <jsp:attribute name="cardBlock">

                        <tags-account:accountTabsComonent>
                            <jsp:attribute name="tabs">
                                <tags-account:tab tabId="edit" tabName="Product Edit" active="true"/>
                                <tags-account:tab tabId="seo" tabName="SEO Metadata"/>
                                <c:if test="${product.uuid != null}">
                                    <c:if test="${product.variantType eq 'StyleVariantProduct'}">
                                        <tags-account:tab tabId="images" tabName="Pictures"/>
                                    </c:if>
                                    <tags-account:tab tabId="variants" tabName="Variants"/>
                                </c:if>
                            </jsp:attribute>
                            <jsp:attribute name="tabsBody">
                                <tags-account:tabContent tabId="edit" active="true">
                                    <jsp:include page="editProductTab.jsp"/>
                                </tags-account:tabContent>
                                <tags-account:tabContent tabId="seo">
                                    <jsp:include page="seoProductTab.jsp"/>
                                </tags-account:tabContent>
                                <c:if test="${product.uuid != null}">
                                    <c:if test="${product.variantType eq 'StyleVariantProduct'}">
                                        <tags-account:tabContent tabId="images">
                                            <jsp:include page="imagesProductTab.jsp"/>
                                        </tags-account:tabContent>
                                    </c:if>
                                    <tags-account:tabContent tabId="variants">
                                        <jsp:include page="variantsProductTab.jsp"/>
                                    </tags-account:tabContent>
                                </c:if>
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
        <div class="md-modal md-effect-13 addcontact" id="modal-variant">
            <div class="md-content">
                <h3 class="f-26">Add Product</h3>
                <div>
                    <form action="<c:url value="/product/${product.uuid}/variant/new"/>" method="get">

                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="icofont icofont-user"></i>
                                </span>
                            </div>
                            <input type="text" name="code" class="form-control pname" placeholder="Code">

                        </div>
                        <div class="input-group">
                            <select name="variantType" class="form-control">
                                <c:forEach var="type" items="${variantTypes}">
                                    <option value="${type}">${type}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="text-center">
                            <button type="submit"
                                    class="btn btn-primary waves-effect m-r-20 f-w-600 d-inline-block save_btn">
                                Create
                            </button>
                            <button type="button"
                                    class="btn btn-primary waves-effect m-r-20 f-w-600 md-close d-inline-block close_btn">
                                Close
                            </button>
                        </div>
                    </form>

                </div>
            </div>
        </div>
        <div class="md-modal md-effect-13 addcontact" id="modal-image">
            <div class="md-content">
                <h3 class="f-26">Add Image to product ${product.code}</h3>
                <div>
                    <form action="<c:url value="/product/${product.uuid}/image/new"/>" method="post" enctype="multipart/form-data">

                        <div class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="icofont icofont-image"></i>
                                </span>
                            </div>
                            <input type="file" name="image" class="form-control pname" placeholder="Image">
                        </div>
                        <form-tags:multiLanguageInput
                                placeholder="Alt"
                                name="alt"
                                icon="icofont icofont-copy-alt">
                        </form-tags:multiLanguageInput>
                        <div class="text-center">
                            <button type="submit"
                                    class="btn btn-primary waves-effect m-r-20 f-w-600 d-inline-block save_btn">
                                Create
                            </button>
                            <button type="button"
                                    class="btn btn-primary waves-effect m-r-20 f-w-600 md-close d-inline-block close_btn">
                                Close
                            </button>
                        </div>
                    </form>

                </div>
            </div>
        </div>
        <div class="md-overlay"></div>
    </div>
</div>