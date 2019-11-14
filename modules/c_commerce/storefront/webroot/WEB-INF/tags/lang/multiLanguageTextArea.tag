<%--@elvariable id="locales" type="java.util.List<com.coretex.items.core.LocaleItem>"--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="message" required="true" type="java.lang.String" %>
<%@ attribute name="messageText" required="true" type="java.lang.String" %>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="inputId" required="false" type="java.lang.String" %>
<%@ attribute name="map" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>

<div class="control-group">
    <label><s:message code="${message}" text="${messageText}"/></label>
    <c:forEach items="${locales}" var="lang"
               varStatus="status">
        <div class="input-group mb-2">
            <div class="input-group-prepend">
                <span class="input-group-text text-monospace">[${lang.iso}]</span>
            </div>
            <form:textarea id="${inputId}_${status.index}" path="${path}['${lang.iso}']"
                           cssClass="form-control input-large"/>
        </div>
        <jsp:doBody/>
        <script type="text/javascript">
            ClassicEditor
                .create(document.getElementById( '${inputId}_${status.index}' ))
                .catch(error => {
                    console.log(error);
                });
        </script>
    </c:forEach>
</div>