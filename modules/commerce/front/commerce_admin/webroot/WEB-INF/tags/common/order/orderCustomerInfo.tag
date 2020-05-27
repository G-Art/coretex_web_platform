<%@ taglib prefix="common-components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<%@ attribute name="customer" required="true" type="com.coretex.commerce.data.CustomerData" %>

<common-components:cardBlock title="${customer.firstName} ${customer.lastName}"
                             description="Short customer information"
                             fullCardButton="false"
                             minimizeCardButton="false">
    <jsp:attribute name="cardBlock">

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
                                            <th scope="row">Full Name</th>
                                            <td>${customer.firstName}&nbsp;${customer.lastName}</td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Email</th>
                                            <td>${customer.email}</td>
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
                                            <th scope="row">Active</th>
                                            <td>
                                                <tags-common:activeLable active="${customer.active}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row">Default language</th>
                                            <td>${customer.language.name}</td>
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

    </jsp:attribute>

</common-components:cardBlock>