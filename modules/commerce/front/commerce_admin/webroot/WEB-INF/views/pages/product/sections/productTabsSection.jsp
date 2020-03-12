<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%--@elvariable id="stores" type="java.util.List<com.coretex.commerce.data.StoreData>"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags-account" tagdir="/WEB-INF/tags/account" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>

<div class="row">
    <div class="col-lg-12">
        <form id="productForm" action="<c:url value="/product/save"/>" method="post" class="md-float-material card-block">
            <input type="hidden" name="uuid" value="${product.uuid}">
            <components:cardBlock title="Product"
                                  description="${product.uuid == null ? 'Create' : 'Edit - '.concat(product.uuid)}">
                    <jsp:attribute name="cardBlock">

                        <tags-account:accountTabsComonent>
                            <jsp:attribute name="tabs">
                                <tags-account:tab tabId="edit" tabName="Product Edit" active="true"/>
                                <tags-account:tab tabId="seo" tabName="SEO Metadata"/>
                                <c:if test="${product.uuid != null}">
                                    <tags-account:tab tabId="images" tabName="Pictures"/>
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
                                    <tags-account:tabContent tabId="images">
                                        <jsp:include page="imagesProductTab.jsp"/>
                                    </tags-account:tabContent>
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
                                    <button type="submit"
                                            class="btn btn-warning waves-effect waves-light">
                                        Discard
                                    </button>
                                </div>
                            </div>
                        </div>
                    </jsp:attribute>
            </components:cardBlock>
        </form>

    </div>
</div>