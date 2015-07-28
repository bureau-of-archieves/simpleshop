<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="displayFormat" required="false" %>
<%@attribute name="hideEmpty" type="java.lang.Boolean" required="false" %>

<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:if test="${empty displayFormat}">
    <c:set var="displayFormat" value="${f:fmd(modelName, path).displayFormat}"/>
</c:if>

<c:set var="fieldRef" value="${base}${path}"/>

<c:set var="hideEmptyExpr" value=""/>
<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-show='${fieldRef}'"/>
</c:if>

<div ${hideEmptyExpr} class="col-sm-6 details-field">
    <div class="row">
        <div class="col-sm-12">
            <label>${label}</label>
        </div>
    </div>
    <div class="row" data-ng-repeat="(key, value) in ${fieldRef}" >
        <div class="col-sm-5"><span>{{key}}</span></div>
        <div class="col-sm-7"><span>{{value}}</span></div>
    </div>


</div>