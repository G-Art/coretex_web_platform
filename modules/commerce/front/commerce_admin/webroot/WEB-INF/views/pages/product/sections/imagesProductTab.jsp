<%--@elvariable id="product" type="com.coretex.commerce.data.forms.ProductForm"--%>
<%--@elvariable id="stores" type="java.util.List<com.coretex.commerce.data.StoreData>"--%>
<%--@elvariable id="images" type="java.util.List<com.coretex.items.cx_core.ProductImageItem>"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form-tags" tagdir="/WEB-INF/tags/common/form" %>
<div class="card">
    <div class="card-header">
        <button type="button"
                class="btn btn-primary waves-effect waves-light f-right d-inline-block md-trigger"
                data-modal="modal-image">
            <i class="icofont icofont-plus m-r-5"></i> Add Image
        </button>
    </div>
    <div class="md-float-material card-block">

        <c:forEach items="${images}" var="image">
            <div class="row p-t-10 p-b-10">

                <div class="col-lg-2 col-md-3 col-sm-6">
                    <img src="\v1${image.productImageUrl}"
                         class="img-fluid width-100 m-b-20" alt="img-edit">
                </div>
                <div class="col-lg-9 col-md-6 col-sm-12">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                <span class="input-group-text">
                                    <i class="icofont icofont-all-caps"></i>
                                </span>
                                </div>
                                <input type="text" name="alt" class="form-control" value="${image.altTag}"
                                       placeholder="Alt">
                            </div>
                                <%--                        <div class="col-xs-6 edit-left">--%>
                                <%--                            <div class="form-radio">--%>
                                <%--                                <form>--%>
                                <%--                                    <div class="radio radiofill">--%>
                                <%--                                        <label>--%>
                                <%--                                            <input type="radio" name="radio"><i--%>
                                <%--                                                class="helper"></i>Largest Image--%>
                                <%--                                        </label>--%>
                                <%--                                    </div>--%>
                                <%--                                    <div class="radio radiofill">--%>
                                <%--                                        <label>--%>
                                <%--                                            <input type="radio" name="radio"><i--%>
                                <%--                                                class="helper"></i>Medium Image--%>
                                <%--                                        </label>--%>
                                <%--                                    </div>--%>
                                <%--                                    <div class="radio radiofill">--%>
                                <%--                                        <label>--%>
                                <%--                                            <input type="radio" name="radio"><i--%>
                                <%--                                                class="helper"></i>Small Image--%>
                                <%--                                        </label>--%>
                                <%--                                    </div>--%>
                                <%--                                </form>--%>
                                <%--                            </div>--%>
                                <%--                        </div>--%>
                            <div class="col-xs-6 edit-right text-right">
                                <a href="<c:url value="/image/remove/${image.uuid}"/>"
                                   class="btn btn-danger waves-effect waves-light">
                                    Remove
                                    <i class="icofont icofont-close-circled f-16 m-l-5"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <hr>
        </c:forEach>
    </div>
</div>


