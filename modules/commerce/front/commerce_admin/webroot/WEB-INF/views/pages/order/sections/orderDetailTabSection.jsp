<%--@elvariable id="order" type="com.coretex.commerce.data.OrderData"--%>

<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="row">
    <script>
        $('.accordian-body').on('show.bs.collapse', function () {
            $(this).closest("table")
                .find(".collapse.in")
                .not(this)
                .collapse('toggle')
        })
    </script>

    <div class="table-responsive-md col-lg-6 col-xl-6 col-md-12 col-sm-12">
        <table class="table table-styling table-hover table-striped ">
            <thead>
            <tr>
                <th>Name</th>
                <th>Price</th>
                <th>Amount</th>
                <th>Total price</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="entity" items="${order.entries}" varStatus="status">
                <tr data-toggle="collapse" data-target="#line${status.index}" class="accordion-toggle">
                    <td>${entity.product.name}</td>
                    <td>${order.currency.symbol}${entity.totalPrice}</td>
                    <td>${entity.quantity}</td>
                    <td>${order.currency.symbol}${entity.totalPrice}</td>
                </tr>
                <tr>
                    <td colspan="4" class="hiddenRow">
                        <div class="accordian-body collapse" id="line${status.index}">
                            <c:if test="${entity.product != null}">
                                <c:set var="originalProduct" value="${entity.product}"/>
                                <div class="row">
                                    <div class="col-4">
                                        <div class="masonry-media">
                                            <a class="media-middle" href="javascript:void(0);">
                                                <img class="img-fluid img-thumbnail"
                                                     src="${applicationBaseUrl}/v1${originalProduct.images[0].path}"
                                                     alt="${originalProduct.name}">
                                            </a>
                                        </div>
                                    </div>
                                    <div class="col-8">
                                        <div class="row">
                                            <div class="col-3">SKU:</div>
                                            <div class="col-9">${originalProduct.code}</div>
                                        </div>
                                        <div class="row">
                                            <div class="col-3">Name:</div>
                                            <div class="col-9">${originalProduct.name}</div>
                                        </div>
                                        <div class="row">
                                            <div class="col-3">Store:</div>
                                            <div class="col-9">${originalProduct.store}</div>
                                        </div>
                                        <div class="row">
                                            <div class="col-3">Available:</div>
                                            <div class="col-9">${originalProduct.available}</div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>
                <tr style="border-top-style: solid; border-color: #01a9ac; border-top-width: 3px;">
                    <th colspan="3" scope="row">
                        Total
                    </th>
                    <td>${order.currency.symbol}${order.total}</td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="col-lg-6 col-xl-6 col-md-12 col-sm-12">

    </div>
</div>