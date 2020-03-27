<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%--@elvariable id="stores" type="java.util.List<com.coretex.commerce.data.StoreData>"--%>
<%--@elvariable id="baseProduct" type="com.coretex.items.cx_core.ProductItem"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>
<div class="row">
    <div class="col-sm-6">
        <div class="input-group">
            <div class="input-group-prepend">
                    <span class="input-group-text">
                        <i class="icofont icofont-ui-cart"></i>
                    </span>
            </div>
            <input type="text" class="form-control"
                   name="code" value="${product.code}"
                   placeholder="Code">
        </div>
    </div>
    <div class="col-sm-6">
        <div class="input-group">
            <input type="checkbox" class="js-success"
                   name="available" ${product.available ? 'checked' : ''}
                   placeholder="Available">
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-6">
        <div class="input-group">
            <div class="input-group-prepend">
                    <span class="input-group-text">
                        <i class="icofont icofont-ui-note"></i>
                    </span>
            </div>
            <select name="store" class="form-control">
                <option ${empty product.store ? 'selected' : ''}
                        value="">Select store
                </option>
                <c:forEach var="store" items="${stores}">
                    <option ${store.uuid eq product.store ? 'selected' : ''}
                            value="${store.uuid}">${store.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="input-group">
            <div class="input-group-prepend">
                    <span class="input-group-text">
                        <i class="icofont icofont-align-left"></i>
                    </span>
            </div>

            <select name="category" class="form-control">
                <option value="">Select category</option>
                <c:forEach var="cat" items="${categories}">
                    <option ${product.category eq cat.uuid ? 'selected' : ''}
                            value="${cat.uuid}">${cat.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>

</div>
<div class="row">
    <div class="col-sm-6">
        <form-tags:multiLanguageInput
                placeholder="Name"
                name="name"
                icon="icofont icofont-copy-alt"
                dataMap="${product.name}">

        </form-tags:multiLanguageInput>
    </div>
    <div class="col-sm-6">
        <form-tags:multiLanguageTextArea
                placeholder="Description"
                name="description"
                icon="icofont icofont-copy-alt"
                dataMap="${product.description}">

        </form-tags:multiLanguageTextArea>
    </div>
</div>

<div class="row">
    <div class="col-sm-6">
        <div class="input-group">
            <div class="input-group-prepend">
                    <span class="input-group-text">
                        <i class="icofont icofont-align-left"></i>
                    </span>
            </div>

            <select name="manufacturer" class="form-control">
                <option value="">Select category</option>
                <c:forEach var="manf" items="${manufacturers}">
                    <option ${product.manufacturer eq manf.uuid ? 'selected' : ''}
                            value="${manf.uuid}">${manf.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
</div>
<hr/>
<c:if test="${not empty baseProduct}">
    <jsp:include page="../variant/${product.variantType}.jsp"/>
</c:if>


