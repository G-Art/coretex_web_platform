<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/responsive/components" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="itemType" required="true" type="com.coretex.data.MetaTypeHierarchyItemDTO" %>

<b-form-select-option value="${itemType.uuid}">${itemType.typeCode}</b-form-select-option>
<c:forEach items="${itemType.subtypes}" var="subType">
    <components:typeHierarchyItem itemType="${subType}"/>
</c:forEach>
