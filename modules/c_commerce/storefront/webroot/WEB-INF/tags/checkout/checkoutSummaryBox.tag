<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>

<!-- order summary box -->
<div class="checkout-box">
<span id="summaryBox" class="box-title">
<p class="p-title"><s:message code="label.order.summary" text="Order summary"/></p>
</span>

    <table id="summary-table" class="table table-condensed table-hover">
        <thead>
        <tr>
            <th width="45%"><s:message code="label.order.item" text="Item"/></th>
            <!--<th width="15%"><s:message code="label.quantity" text="Quantity" /></th>-->
            <th width="20%"><s:message code="label.order.price" text="Price"/></th>
            <th width="20%"><s:message code="label.order.total" text="Total"/></th>
        </tr>
        </thead>

        <tbody id="summaryRows">
        <c:forEach items="${cart.shoppingCartItems}" var="shoppingCartItem">
            <tr class="item">
                <td width="38%">
                        ${shoppingCartItem.quantity} x ${shoppingCartItem.name}
                    <c:if test="${fn:length(shoppingCartItem.shoppingCartAttributes)>0}">
                        <br/>
                        <ul>
                            <c:forEach items="${shoppingCartItem.shoppingCartAttributes}"
                                       var="option">
                                <li>${option.optionName} - ${option.optionValue}</li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </td>
                <!--<td width="15%">${shoppingCartItem.quantity}</td>-->
                <td width="31%"><strong>${shoppingCartItem.price}</strong></td>
                <td width="31%"><strong>${shoppingCartItem.subTotal}</strong></td>
            </tr>
        </c:forEach>
        <!-- subtotals -->
        <c:forEach items="${order.orderTotalSummary.totals}" var="total">
            <c:if test="${total.orderTotalCode!='order.total.total'}">


                <tr class="subt">
                    <td colspan="2">
                        <c:choose>
                            <c:when test="${total.orderTotalCode=='order.total.discount'}">
                                <s:message code="label.generic.rebate" text="Rebate"/>&nbsp;-&nbsp;<s:message
                                    code="${total.text}" text="${total.text}"/>
                            </c:when>
                            <c:otherwise>
                                <s:message code="${total.orderTotalCode}"
                                           text="${total.orderTotalCode}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><strong><c:choose><c:when
                            test="${total.orderTotalCode=='order.total.discount'}"><font
                            color="red">- <sm:monetary
                            value="${total.value}"/></font></c:when><c:otherwise><sm:monetary
                            value="${total.value}"/></c:otherwise></c:choose></strong></td>
                </tr>


            </c:if>
        </c:forEach>
        </tbody>
    </table>


    <div id="totalRow" class="total-box">
												<span class="total-box-grand-total">
													<font class="total-box-label">
													<s:message code="order.total.total" text="Total"/>&nbsp;
													<font class="total-box-price grand-total"><sm:monetary
                                                            value="${order.orderTotalSummary.total}"/></font>
													</font>
												</span>
    </div>
</div>
<!-- end order summary box -->