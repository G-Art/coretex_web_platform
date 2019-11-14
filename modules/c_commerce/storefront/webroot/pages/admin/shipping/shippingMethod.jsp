<%--@elvariable id="deliveryService" type="com.coretex.items.commerce_core_model.DeliveryServiceItem"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page session="false" %>

<script>


</script>


<div class="tabbable">


    <jsp:include page="/common/adminTabs.jsp"/>

    <div class="tab-content">


        <div class="tab-pane active" id="shipping-method">


            <div class="sm-ui-component">

                <s:message code="label.shipping.title" text="Shipping configuration"/> -
                <c:import url="${includeFragment}"/>

            </div>


        </div>


    </div>

</div>