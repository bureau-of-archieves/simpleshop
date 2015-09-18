<%@include file="../_header.tag" %>
<%@attribute name="path" required="true" %>
<%@attribute name="childModelName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="itemName" required="false" %>
<%@attribute name="title" required="false" %>


<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:if test="${empty parentId}">
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}"/>
</c:if>

<c:if test="${empty itemName}">
    <c:set var="itemName" value="item"/>
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}"/>

<c:set var="fieldRef" value="${base}${path}"/>

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}"/>

<c:if test="${empty childModelName}">
    <c:set var="childModelName" value="${f:collectionModelName(propertyMetadata)}"/>
</c:if>

<div class="col-sm-6 inline-list details-inline-list">

    <div class="row">
        <div class="col-sm-5"><label> ${label}</label></div>
        <div class="col-sm-7 clearfix"  <c:if test="${not empty title}"> title="${title}" </c:if>>

            <span class="pull-left" data-ng-repeat="${itemName} in ${fieldRef}">
                <jsp:doBody/>
            </span>
            <span data-ng-show="${fieldRef}.length == 0" >NONE</span>

        </div>
    </div>
</div>





