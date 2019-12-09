<%--@elvariable id="order" type="com.coretex.commerce.admin.data.OrderData"--%>

<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="row">
    <div class="col-lg-6">
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
            <c:forEach var="product" items="${order.orderProducts}">
                <tr>
                    <td>${product.productName}</td>
                    <td>${order.currency.symbol}${product.oneTimeCharge}</td>
                    <td>${product.productQuantity}</td>
                    <td>${order.currency.symbol}${product.productQuantity * product.oneTimeCharge}</td>
                </tr>
            </c:forEach>
            <c:forEach var="orderTotal" items="${order.orderTotal}">
                <tr>
                    <th colspan="3" scope="row">
                        <s:message code="${orderTotal.orderTotalCode}" text="${orderTotal.title}"/>
                    </th>
                    <td>${order.currency.symbol}${orderTotal.value}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>