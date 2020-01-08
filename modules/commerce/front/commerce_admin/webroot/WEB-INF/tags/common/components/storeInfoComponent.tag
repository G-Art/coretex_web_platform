<%--@elvariable id="currentUser" type="com.coretex.commerce.data.UserData"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="common-components" tagdir="/WEB-INF/tags/common/components" %>
<div class="row">
    <div class="col-sm-12">
        <common-components:cardBlock title="Store Info"
                                     description="Information about current store">
            <jsp:attribute name="cardBlock">
                <c:set var="store" value="${currentUser.merchantStore}"/>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="general-info">
                            <div class="row">
                                <div class="col-lg-12 col-xl-6">
                                    <div class="table-responsive">
                                        <table class="table m-0">
                                            <tbody>
                                            <tr>
                                                <th scope="row">Store name</th>
                                                <td>${store.storeName}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Store address</th>
                                                <td>${store.storeAddress}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">City</th>
                                                <td>${store.storeCity}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Postal code</th>
                                                <td>${store.storePostalCode}</td>
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
                                                <th scope="row">Domain</th>
                                                <td><a target="_blank"
                                                       href="${store.domainName}">${store.domainName}</a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Email</th>
                                                <td>${store.storeEmailAddress}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Phone</th>
                                                <td>${store.storePhone}</td>
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
            </jsp:attribute>

        </common-components:cardBlock>
    </div>
</div>