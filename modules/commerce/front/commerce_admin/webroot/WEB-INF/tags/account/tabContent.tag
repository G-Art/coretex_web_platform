<%@ attribute name="tabId" required="true" %>
<%@ attribute name="active" required="false" %>
<div class="tab-pane ${not empty active && active ? 'active' : ''}" id="${tabId}" role="tabpanel">
   <jsp:doBody/>
</div>