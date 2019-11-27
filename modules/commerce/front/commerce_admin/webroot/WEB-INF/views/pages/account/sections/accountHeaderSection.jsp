<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row">
    <div class="col-lg-12">
        <div class="cover-profile">
            <div class="profile-bg-img">
                <img class="profile-bg-img img-fluid"
                     src="<c:url value="/resources/assets/images/user-profile/bg-img1.jpg"/>" alt="bg-img">
                <div class="card-block user-info">
                    <div class="col-md-12">
                        <div class="media-left">
                            <a href="javascript:void(0);" class="profile-image">
                                <img class="user-img img-radius" height="108px"
                                     src="<c:url value="/resources/assets/images/social/profile.jpg"/>" alt="user-img">
                            </a>
                        </div>
                        <div class="media-body row">
                            <div class="col-lg-12">
                                <div class="user-title">
                                    <h2>${user.firstName}&nbsp;${user.lastName}</h2>
                                    <span class="text-white">${user.adminName}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>