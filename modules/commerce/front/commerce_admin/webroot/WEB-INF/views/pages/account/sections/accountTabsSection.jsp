<%--@elvariable id="user" type="com.coretex.items.commerce_core_model.UserItem"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags-account" tagdir="/WEB-INF/tags/account" %>
<%@ taglib prefix="tags-common" tagdir="/WEB-INF/tags/common" %>
<div class="row">
    <div class="col-lg-12">

        <tags-account:accountTabsComonent>
            <jsp:attribute name="tabs">
                <tags-account:tab tabId="personal" tabName="Personal Info" active="true"/>
                <tags-account:tab tabId="scurity" tabName="Security"/>
            </jsp:attribute>
            <jsp:attribute name="tabsBody">
                <tags-account:tabContent tabId="personal" active="true">
                    <!-- personal card start -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-header-text">About Me</h5>
                        <button id="edit-btn" type="button"
                                class="btn btn-sm btn-primary waves-effect waves-light f-right">
                            <i class="icofont icofont-edit" data-view='view-info' data-edit='edit-info'></i>
                        </button>
                    </div>
                    <div class="card-block">
                        <jsp:include page="userInfoBlock.jsp"/>
                        <jsp:include page="userEditInfoBlock.jsp"/>
                    </div>
                    <!-- end of card-block -->
                </div>
                <!-- personal card end-->
                </tags-account:tabContent>
                <tags-account:tabContent tabId="scurity">
                <!-- info card start -->
                <tags-common:comingSoonCart/>
                <!-- info card end -->
                </tags-account:tabContent>
            </jsp:attribute>
        </tags-account:accountTabsComonent>
    </div>
</div>