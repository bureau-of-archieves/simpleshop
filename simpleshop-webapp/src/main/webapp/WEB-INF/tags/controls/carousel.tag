<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="hideEmpty" type="java.lang.Boolean" required="false" %>
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

<c:set var="fieldRef" value="${base}${path}"/>

<c:set var="hideEmptyExpr" value=""/>
<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-hide='${fieldRef} == null || ${fieldRef}.length == 0'"/>
</c:if>

<c:set var="imgBase" value="${f:peek(stack, '_imgBase')}" />

<div ${hideEmptyExpr} class="col-sm-12 carousel-container" >

    <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        No image uploaded yet.
    </div>

    <carousel interval="5000" no-wrap="false">
        <slide ng-repeat="slide in ${fieldRef}" >
            <img data-ng-src="${imgBase}{{slide}}" >
            <div class="carousel-caption">
                <p>${label} <c:if test="${not empty title}"> title="${title}" </c:if></p>
            </div>
        </slide>
    </carousel>

</div>