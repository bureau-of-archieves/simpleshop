<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="targetModelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="hideEmpty" type="java.lang.Boolean" required="false" %>

<%@attribute name="descExpr" required="false" %>

<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:if test="${empty targetModelName}">
    <c:set var="targetModelName" value="${f:firstCharLower(f:fmd(modelName, path).propertyType)}" />
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:set var="fieldRef" value="${base}${path}"/>

<c:set var="hideEmptyExpr" value=""/>
<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-show='{{${fieldRef}}}'"/>
</c:if>

<c:if test="${not empty descExpr}">
    <c:set var="descExpr" value="${fn:replace(descExpr, '{fieldRef}',fieldRef)}" />
</c:if>

<div ${hideEmptyExpr} class="col-sm-6 details-field" >
    <div class="row">
    <div class="col-sm-5" > <label>${label}</label> </div>

    <div class="col-sm-7" >
    <ctrl:modelLink targetModelName="${targetModelName}" path="${path}" descExpr="${descExpr}"/>&nbsp;
    <jsp:doBody/>
    </div>
    </div>
</div>


