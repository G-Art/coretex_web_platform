<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%--@elvariable id="stores" type="java.util.List<com.coretex.commerce.data.StoreData>"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>

    <div class="row">
        <div class="col-sm-6">
            <form-tags:multiLanguageInput
                    placeholder="Title"
                    name="title"
                    icon="icofont icofont-copy-alt"
                    dataMap="${product.title}">

            </form-tags:multiLanguageInput>
        </div>
        <div class="col-sm-6">
            <form-tags:multiLanguageTextArea
                    placeholder="Meta Keywords"
                    name="metaKeywords"
                    icon="icofont icofont-copy-alt"
                    dataMap="${product.metaKeywords}">

            </form-tags:multiLanguageTextArea>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-6">
            <form-tags:multiLanguageTextArea
                    placeholder="Meta description"
                    name="metaDescription"
                    icon="icofont icofont-copy-alt"
                    dataMap="${product.metaDescription}">

            </form-tags:multiLanguageTextArea>
        </div>
    </div>
