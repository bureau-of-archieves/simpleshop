<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="optionsUrl" required="true" %>
<%@attribute name="optionsExpression" required="true" %>
<%@attribute name="itemsVariableName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>

<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>

<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>

<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>

<c:if test="${empty itemsVariableName}" >
    <c:set var="itemsVariableName" value="items" />
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}" />
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}" />

<c:url var="url" value="${optionsUrl}" />

<div class="form-group" data-spg-load-list="${itemsVariableName},${url}">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <select id="${id}" name="${path}" class="form-control" data-ng-model="${base}${path}" data-ng-options="${optionsExpression}" >
        </select>
    </div>
</div>