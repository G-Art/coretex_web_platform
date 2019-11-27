<%--@elvariable id="currentUser" type="com.coretex.commerce.admin.data.UserData"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row">
    <div class="col-sm-12">
        <div class="card">
            <div class="card-header">
                <h5>Store Info</h5>
                <span>Information about current store</span>
                <div class="card-header-right">
                    <ul class="list-unstyled card-option">
                        <li><i class="feather icon-maximize full-card"></i></li>
                        <li><i class="feather icon-minus minimize-card"></i></li>
                        <%--                                    <li><i class="feather icon-trash-2 close-card"></i></li>--%>
                    </ul>
                </div>
            </div>

            <c:set var="store" value="${currentUser.merchantStore}"/>
            <div class="card-block">
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
            </div>
        </div>
    </div>
</div>