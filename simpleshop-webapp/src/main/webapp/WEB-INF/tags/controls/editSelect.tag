<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
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

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />

<c:if test="${empty label}">
    <c:set var="label" value="${propertyMetadata.label}" />
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}" />


<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <select id="${id}" name="${path}" class="form-control" data-ng-model="${base}${path}" >
            <c:forEach var="pair" items="${f:options(propertyMetadata.returnType)}">
               <option value="${pair.key}" >${pair.value}</option>
            </c:forEach>
        </select>
    </div>
</div>