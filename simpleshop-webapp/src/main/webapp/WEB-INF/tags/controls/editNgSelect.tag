<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="text" required="false" %>
<%@attribute name="parentId" required="false" %>

<%@attribute name="init" required="true" %>
<%@attribute name="options" required="true" %>


<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>

<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>

<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>

<c:if test="${empty text}">
    <c:set var="text" value="${f:fmd(modelName, path).label}" />
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}" />


<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${text}</label>

    <div class="col-sm-9">
        <select id="${id}" name="${path}" class="form-control" data-ng-model="${base}${path}" data-ng-init="${init}"  data-ng-options="${options}" >
        </select>
    </div>
</div>