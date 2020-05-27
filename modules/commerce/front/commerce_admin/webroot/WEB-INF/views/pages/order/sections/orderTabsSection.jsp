<%--@elvariable id="order" type="com.coretex.commerce.data.AbstractOrderData"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags-account" tagdir="/WEB-INF/tags/account" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="common-components" tagdir="/WEB-INF/tags/common/components" %>
<div class="row">
    <div class="col-lg-12">
        <common-components:cardBlock title="Order summary"
                                     description="Main information about order">
            <jsp:attribute name="cardBlock">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="general-info">
                            <div class="row">
                                <div class="col-lg-12 col-xl-6">
                                    <div class="table-responsive">
                                        <table class="table m-0">
                                            <tbody>
                                                <tr>
                                                    <th scope="row">UUID</th>
                                                    <td>${order.uuid}</td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">Store</th>
                                                    <td>${order.store.name}</td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">Total</th>
                                                    <td>${order.currency.symbol}${order.total}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <!-- end of table col-lg-6 -->
                                <div class="col-lg-12 col-xl-6">
                                    <div class="table-responsive">
                                        <table class="table">
                                            <tbody>
                                                <tr>
                                                    <th scope="row">Date</th>
                                                    <td>${order.date}</td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">Email</th>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty order.address}">
                                                                <c:set var="email" value="${order.address.email}"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:if test="${not empty order.customer}">
                                                                    <c:set var="email" value="${order.customer.email}"/>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>

                                                        <c:if test="${not empty email}">
                                                            <a href="mailto:${email}">${email}</a>
                                                        </c:if>

                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">Phone</th>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty order.address}">
                                                                <c:set var="phone" value="${order.address.phone}"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:if test="${not empty order.customer}">
                                                                    <c:set var="phone" value="${order.customer.delivery.phone}"/>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${not empty phone}">
                                                            <a href="tel:${phone}">${phone}</a>
                                                        </c:if>

                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <!-- end of table col-lg-6 -->
                            </div>
                            <!-- end of row -->
                        </div>
                        <!-- end of general info -->
                    </div>
                    <!-- end of col-lg-12 -->
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <tags-account:accountTabsComonent>
                            <jsp:attribute name="tabs">
                                <tags-account:tab tabId="datail" tabName="Detail" active="true"/>
                                <tags-account:tab tabId="customer" tabName="Customer" />
                                <tags-account:tab tabId="delivery" tabName="Delivery"/>
                                <tags-account:tab tabId="payment" tabName="Payment"/>
                            </jsp:attribute>
                            <jsp:attribute name="tabsBody">
                                <tags-account:tabContent tabId="datail" active="true">
                                    <jsp:include page="orderDetailTabSection.jsp"/>
                                </tags-account:tabContent>
                                <tags-account:tabContent tabId="customer" >
                                    <jsp:include page="orderCustomerTabSection.jsp"/>
                                </tags-account:tabContent>
                                <tags-account:tabContent tabId="delivery">
                                    <jsp:include page="orderDeliveryTabSection.jsp"/>
                                </tags-account:tabContent>
                                <tags-account:tabContent tabId="payment">
                                    <tags-common:comingSoonCart/>
                                </tags-account:tabContent>
                            </jsp:attribute>
                        </tags-account:accountTabsComonent>
                    </div>
                </div>
            </jsp:attribute>

        </common-components:cardBlock>

    </div>
</div>