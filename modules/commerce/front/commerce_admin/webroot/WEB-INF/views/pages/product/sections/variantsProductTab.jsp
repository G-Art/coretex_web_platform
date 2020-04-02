<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%--@elvariable id="stores" type="java.util.List<com.coretex.commerce.data.StoreData>"--%>
<%--@elvariable id="variantTypes" type="java.util.Set<String>"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/common/components" %>
<c:if test="${not empty product.uuid}">
    <components:tableComponent title="All products variants"
                               description="List of product variants"
                               tableId="productVariantTable"
                               dataSourceLink="/product/variant/${product.uuid}/paginated"
                               rowId="uuid"
                               actionTarget="5"
                               actionPath="/product/${product.uuid}/variant"
                               deleteActionPath="/product/remove" >
        <jsp:attribute name="cardHeader">
                    <button type="button"
                            class="btn btn-primary waves-effect waves-light f-right d-inline-block md-trigger"
                            data-modal="modal-variant">
                        <i class="icofont icofont-plus m-r-5"></i> Add Product
                    </button>
        </jsp:attribute>
        <jsp:attribute name="theader">
                    <tr>
                        <th>Image</th>
                        <th>SKU</th>
                        <th>Name</th>
                        <th>Available</th>
                        <th>Store</th>
                        <th>Action</th>
                    </tr>
                </jsp:attribute>
        <jsp:attribute name="columns">
                    [
                        {"data": "image",
                         "render": function (data, type, row) {
                                if(data){
                                    return  `<img style="max-width: 200px;" src="/v1\${data}" class="img-fluid">`
                                }else {
                                    return  `<img style="max-width: 200px;" src="<c:url
                                                  value="/resources/assets/images/service/no_image.svg"/>" class="img-fluid">`
                                }
                            }
                        },
                        {"data": "code"},
                        {"data": "name"},
                        {"data": "available"},
                        {"data": "store"}
                    ]
                </jsp:attribute>
    </components:tableComponent>

</c:if>