<%--@elvariable id="mataTypes" type="java.util.List<com.coretex.data.MetaTypeDTO>"--%>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="components" tagdir="/WEB-INF/tags/responsive/components" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:abstractPage pageTitle="Type manager">
    <jsp:attribute name="footerJs">
        <script src="<c:url value="/resources/bootstrap/js/components/itemtypeinfopanel.com.js" />"></script>
        <script src="<c:url value="/resources/bootstrap/js/components/itemtypetableview.com.js" />"></script>
        <script src="<c:url value="/resources/bootstrap/js/items.js" />"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="items">
            <div class="item-info-panel-wrap z-1" v-bind:class="{ open: panelOpen }">
                <div class="toggler p-2" v-on:click="toggleInfoPanel()"><i class="fas fa-dna"></i></div>
                <div class="item-info-panel">
                    <item-type-info-panel v-bind:item="item"
                                          v-on:read-attributes-change="readAttributesChange"
                                          v-on:search-attributes-change="searchAttributesChange"></item-type-info-panel>
                </div>
            </div>
            <div class="row">
                <div class="col-1 pr-0"> <small>Item Type:</small> </div>
                <div class="col-4">
                    <b-form-select v-model="selected" v-on:change="change" size="sm">
                        <b-form-select-option
                                value="${metaTypeHierarchy.root.uuid}">${metaTypeHierarchy.root.typeCode}</b-form-select-option>
                        <c:forEach items="${metaTypeHierarchy.root.subtypes}" var="subType">
                            <components:typeHierarchyItem itemType="${subType}"/>
                        </c:forEach>
                    </b-form-select>
                </div>
            </div>
            <div class="row" v-if="item">
                <div class="col-12">
                    <item-type-table-view v-bind:item="item"
                                          v-bind:display-attributes="readAttributes"></item-type-table-view>
                </div>
            </div>

        </div>

    </jsp:body>
</template:abstractPage>