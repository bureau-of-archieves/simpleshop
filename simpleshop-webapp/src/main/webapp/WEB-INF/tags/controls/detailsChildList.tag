<%-- This control shows a collection of model objects in details view.--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="childModelName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="itemName" required="false" %>
<%@attribute name="label" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}"/>
<c:if test="${empty childModelName}">
    <c:set var="childModelName" value="${f:collectionModelName(propertyMetadata)}"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${propertyMetadata.label}"/>
</c:if>

<c:if test="${empty itemName}">
    <c:set var="itemName" value="item"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<c:set var="id" value="${f:fid(parentId, path)}"/>
<c:set var="fieldRef" value="${base}${path}"/>
<div class="panel panel-default margin-top-1em col-sm-12 details-child-list">
    <div class="panel-heading">
        ${label} &nbsp; <span class="badge">{{${fieldRef}.length}}</span>
    </div>
    <div class="panel-body">

        <div class="panel panel-default" data-ng-repeat="${itemName} in ${fieldRef}">
            <div class="panel-body">
                <dl class="dl-horizontal">
                    <comm:push value="${f:concat(itemName, '.')}" var="base" />
                    <comm:push value="${childModelName}" var="modelName" />
                    <comm:push value="${id}" var="parentId" />
                    <jsp:doBody/>
                    <comm:pop var="base" />
                    <comm:pop var="modelName" />
                    <comm:pop var="parentId" />
                </dl>
            </div>
        </div>

        <div data-ng-hide="{{${fieldRef}.length}}">
            <spring:message code="jsp.literal.thisListIsEmpty" />
        </div>

    </div>
</div>
