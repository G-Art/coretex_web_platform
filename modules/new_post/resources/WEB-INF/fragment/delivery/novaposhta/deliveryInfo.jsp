<%--@elvariable id="order" type="com.coretex.commerce.data.AbstractOrderData"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>

<c:set var="deliveryType" value="${order.deliveryType}"/>
<c:set var="address" value="${order.address}"/>
<c:choose>
    <c:when test="${deliveryType.additionalInfo['sendToWarehouse']}">

        <div class="view-info">
            <div class="row">
                <div class="col-lg-12">
                    <div class="general-info">
                        <div class="row">
                            <div class="col-lg-12 col-xl-6">
                                <div class="table-responsive">
                                    <table class="table m-0">
                                        <tbody>
                                        <tr>
                                            <th scope="row">Type</th>
                                            <td>
                                                <img style="height: 18px;" src="<c:url value="/resources/logo/N_P_logo.svg" />"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Name</th>
                                            <td>${deliveryType.name['en']}</td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Active</th>
                                            <td>
                                                <tags-common:activeLable active="${deliveryType.active}"/>
                                            </td>
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
                                            <th scope="row">Name</th>
                                            <td>
                                                    ${address.firstName}&nbsp;${address.lastName}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Phone</th>
                                            <td>
                                                    ${address.phone}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">City</th>
                                            <td>
                                                    ${address.city}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Warehouse</th>
                                            <td>${address.additionalInfo['branch']}</td>
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
            <!-- end of row -->
        </div>
    </c:when>
    <c:otherwise>
        <div class="view-info">
            <div class="row">
                <div class="col-lg-12">
                    <div class="general-info">
                        <div class="row">
                            <div class="col-lg-12 col-xl-6">
                                <div class="table-responsive">
                                    <table class="table m-0">
                                        <tbody>
                                        <tr>
                                            <th scope="row">Type</th>
                                            <td>
                                                <img style="height: 18px;" src="<c:url value="/resources/logo/N_P_logo.svg" />"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Name</th>
                                            <td>${deliveryType.name['en']}</td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Active</th>
                                            <td>
                                                <tags-common:activeLable active="${deliveryType.active}"/>
                                            </td>
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
                                            <th scope="row">Name</th>
                                            <td>
                                                    ${address.firstName}&nbsp;${address.lastName}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Phone</th>
                                            <td>
                                                    ${address.phone}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Address line 1</th>
                                            <td>
                                                    ${address.addressLine1}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Address line 2</th>
                                            <td>
                                                    ${address.addressLine2}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Postal code</th>
                                            <td>
                                                    ${address.postalCode}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Settlement</th>
                                            <td>
                                                    ${address.city}
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
            <!-- end of row -->
        </div>
    </c:otherwise>
</c:choose>