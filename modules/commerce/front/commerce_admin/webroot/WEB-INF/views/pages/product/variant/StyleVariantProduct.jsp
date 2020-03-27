<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                   name="colorCode" value="${product.colorCode}"
                   placeholder="Color code">
        </div>
    </div>
    <div class="col-sm-6">
        <form-tags:multiLanguageInput
                placeholder="Color name"
                name="colorName"
                icon="icofont icofont-copy-alt"
                dataMap="${product.colorName}">
        </form-tags:multiLanguageInput>
    </div>
</div>