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
            <common-nav:subMenuItem name="Commerce" menuClass="pcoded-hasmenu" iconClass="icofont icofont-cart-alt"
                                    trigger="${
                                    cp.concat('/order') eq requestPath ||
                                    cp.concat('/cart') eq requestPath
                                    }">
                <common-nav:subMenuItem name="Orders" link="/order"/>
                <common-nav:subMenuItem name="Carts" link="/cart"/>
            </common-nav:subMenuItem>
            <common-nav:subMenuItem name="Content" menuClass="pcoded-hasmenu"
                                    iconClass="zmdi zmdi-receipt"
                                    trigger="${
                                    cp.concat('/category') eq requestPath ||
                                    cp.concat('/category/hierarchy') eq requestPath ||
                                    cp.concat('/product') eq requestPath ||
                                    cp.concat('/manufacturer') eq requestPath
                                    }">
                <common-nav:subMenuItem name="Categories" menuClass="pcoded-hasmenu"
                                        trigger="${
                                        cp.concat('/category') eq requestPath ||
                                        cp.concat('/category/hierarchy') eq requestPath
                                        }">
                    <common-nav:subMenuItem name="Category" link="/category"/>
                    <common-nav:subMenuItem name="Category hierarchy" link="/category/hierarchy"/>
                </common-nav:subMenuItem>
                <common-nav:subMenuItem name="Products" menuClass="pcoded-hasmenu"
                                        trigger="${
                                        cp.concat('/product') eq requestPath ||
                                        cp.concat('/manufacturer') eq requestPath
                                        }">
                    <common-nav:subMenuItem name="Product" link="/product"/>
                    <common-nav:subMenuItem name="Manufacturers" link="/manufacturer"/>
                </common-nav:subMenuItem>
            </common-nav:subMenuItem>
        </common-nav:navigationGroup>

        <common-nav:navigationGroup name="Marketing">
            <common-nav:subMenuItem name="Delivery" link="/delivery" iconClass="icofont icofont-vehicle-delivery-van "/>
            <common-nav:subMenuItem name="Payment" link="/payment" iconClass="icofont icofont-money-bag"/>
        </common-nav:navigationGroup>

        <common-nav:navigationGroup name="Users">
            <common-nav:subMenuItem name="Employees" iconClass="icofont icofont-support"
                                    link="/user"/>
            <common-nav:subMenuItem name="Customers" iconClass="zmdi zmdi-accounts-alt"
                                    link="/customer"/>
        </common-nav:navigationGroup>

    </div>
</nav>