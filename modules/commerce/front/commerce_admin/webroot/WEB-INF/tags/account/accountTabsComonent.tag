<%@ attribute name="tabs" required="false" fragment="true" %>
<%@ attribute name="tabsBody" required="false" fragment="true" %>
<!-- tab header start -->
<div class="tab-header card">
    <ul class="nav nav-tabs md-tabs tab-timeline d-flex justify-content-center " role="tablist" id="mytab">
        <jsp:invoke fragment="tabs"/>
    </ul>
</div>
<!-- tab header end -->
<!-- tab content start -->
<div class="tab-content">
    <jsp:invoke fragment="tabsBody"/>
</div>
<!-- tab content end -->