<%@ attribute name="tabId" required="true" %>
<%@ attribute name="tabName" required="true" %>
<%@ attribute name="active" required="false" %>

<li class="nav-item ">
    <a class="nav-link ${not empty active && active ? 'active' : ''}" data-toggle="tab" href="#${tabId}" role="tab">${tabName}</a>
    <div class="slide"></div>
</li>