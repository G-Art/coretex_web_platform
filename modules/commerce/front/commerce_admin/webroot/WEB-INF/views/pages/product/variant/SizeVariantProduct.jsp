<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>
<div class="row">
    <div class="col-sm-6">
        <form-tags:multiLanguageInput
                placeholder="Size code"
                name="size"
                icon="icofont icofont-copy-alt"
                dataMap="${product.size}">
        </form-tags:multiLanguageInput>
    </div>
</div>