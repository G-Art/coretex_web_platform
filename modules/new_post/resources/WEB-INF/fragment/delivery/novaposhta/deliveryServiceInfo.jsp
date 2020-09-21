<%--@elvariable id="order" type="com.coretex.commerce.data.AbstractOrderData"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>


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
                                    <th scope="row">Endpoint</th>
                                    <td>
                                        <div class="input-group">
                                            <input type="text"
                                                   name="city"
                                                   class="form-control"
                                                   value="${ds.additionalData["endpoint"]}">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Api Key</th>
                                    <td>
                                        <div class="input-group">
                                            <input type="text"
                                                   name="apiKey"
                                                   class="form-control"
                                                   value="${ds.additionalData["apiKey"]}">
                                        </div>
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
                                    <th scope="row">Data Format</th>
                                    <td>
                                        <div class="input-group">
                                            <input type="text"
                                                   name="dataFormat"
                                                   class="form-control"
                                                   value="${ds.additionalData["dataFormat"]}">
                                        </div>
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