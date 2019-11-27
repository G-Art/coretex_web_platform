<%@ taglib prefix="common-nav" tagdir="/WEB-INF/tags/common/nav" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<nav class="pcoded-navbar">
    <div class="pcoded-inner-navbar main-menu">
        <common-nav:navigationGroup name="Store">
            <common-nav:subMenuItem name="Main" menuClass="pcoded-hasmenu" iconClass="feather icon-home"
                                    trigger="${
                                    cp.concat('/home') eq requestPath ||
                                    cp.concat('/store') eq requestPath
                                    }">
                <common-nav:subMenuItem name="Home" link="/home"/>
                <common-nav:subMenuItem name="Stores" link="/store"/>
            </common-nav:subMenuItem>
            <common-nav:subMenuItem name="Content" menuClass="pcoded-hasmenu"
                                    iconClass="zmdi zmdi-receipt">
                <common-nav:subMenuItem name="Categories" menuClass="pcoded-hasmenu">
                    <common-nav:subMenuItem name="Categoy" link=""/>
                    <common-nav:subMenuItem name="Categoy hierarchy" link=""/>
                </common-nav:subMenuItem>
                <common-nav:subMenuItem name="Products" menuClass="pcoded-hasmenu">
                    <common-nav:subMenuItem name="Product" link=""/>
                    <common-nav:subMenuItem name="Manufacturers" />
                    <common-nav:subMenuItem name="Brands" />
                </common-nav:subMenuItem>
            </common-nav:subMenuItem>
        </common-nav:navigationGroup>

        <common-nav:navigationGroup name="Users">
            <common-nav:subMenuItem name="Employees" iconClass="icofont icofont-support"/>
            <common-nav:subMenuItem name="Customers" iconClass="zmdi zmdi-accounts-alt"/>
        </common-nav:navigationGroup>

    </div>
</nav>