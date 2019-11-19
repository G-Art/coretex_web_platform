
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<!-- footer -->
<footer id="footer">

    <div class="subfooter">
        <div class="container">
            <div class="row">
                <div class="col-md-6"><sm:storeCopy/></div>
                <div class="col-md-6">
                    <div id="navbar-collapse-2" class="collapse navbar-collapse">
                        <ul class="nav navbar-nav">
                            <li class="<sm:activeLink linkCode="HOME" activeReturnCode="active"/>">
                                <a href="<c:url value="/shop"/>"><s:message code="menu.home" text="Home"/></a>
                            </li>
                            <c:if test="${requestScope.CONFIGS['displayContactUs']==true}">
                                <li class="<sm:activeLink linkCode="CONTACT" activeReturnCode="active"/>"><a
                                        href="<c:url value="/shop/store/contactus.html"/>"><s:message
                                        code="label.customer.contactus" text="Contact us"/></a></li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
</footer>