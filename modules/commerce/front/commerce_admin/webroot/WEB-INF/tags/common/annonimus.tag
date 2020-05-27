<%@ taglib prefix="common-components" tagdir="/WEB-INF/tags/common/components" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<common-components:cardBlock title="Anonymous"
                             description="This customer wants to be anonymous."
                             fullCardButton="false"
                             minimizeCardButton="false">
    <jsp:attribute name="cardBlock">
        <img class="profile-bg-img img-fluid w-25"
             src="<c:url value="/resources/assets/vector/anonim.svg"/>" alt="bg-img">
        <p>
             ANONYMOUS
        </p>
    </jsp:attribute>

</common-components:cardBlock>