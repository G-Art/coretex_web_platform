<%--@elvariable id="locales" type="java.util.List<com.coretex.commerce.data.LocaleData>"--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="icon" type="java.lang.String" %>
<%@ attribute name="name" required="true" type="java.lang.String" %>
<%@ attribute name="placeholder" required="true" type="java.lang.String" %>
<%@ attribute name="dataMap" required="false" type="java.util.Map<java.lang.String, java.lang.String>" %>

<div class="accordion-block">
    <div id="accordion" role="tablist" aria-multiselectable="true">
        <div class="accordion-panel">
            <div class="accordion-heading" role="tab" id="heading${name}One">
                <h3 class="card-title accordion-title" style="background: white;">
                    <a class="accordion-msg scale_active collapsed" data-toggle="collapse" data-parent="#accordion"
                       href="#collapse${name}One" aria-expanded="false" aria-controls="collapse${name}One">

                        <div class="input-group-prepend">
                            <c:if test="${not empty icon}">
                                <span class="input-group-text">
                                    <i class="${icon}"></i>
                                </span>
                            </c:if>
                            <span style="padding: .375rem .75rem;">${placeholder}</span>
                        </div>

                    </a>
                </h3>
            </div>
            <div id="collapse${name}One" class="panel-collapse in collapse" role="tabpanel" aria-labelledby="heading${name}One"
                 style="">
                <div class="accordion-content accordion-desc">
                    <c:forEach items="${locales}" var="loc"
                               varStatus="status">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text text-monospace">
                                                ${loc.isocode}
                                        </span>
                                    </div>
                                    <c:choose>
                                        <c:when test="${not empty dataMap}">
                                            <input type="text" name="${name}['${loc.isocode}']"
                                                   class="form-control" value="${dataMap[loc.isocode]}"
                                                   placeholder="${placeholder}">
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" name="${name}['${loc.isocode}']"
                                                   class="form-control"
                                                   placeholder="${placeholder}">
                                        </c:otherwise>
                                    </c:choose>


                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>



