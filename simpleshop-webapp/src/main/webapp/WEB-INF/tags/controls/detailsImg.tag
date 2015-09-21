<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="uploadUrl" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>

<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>
<c:set var="imgBase" value="${f:peek(stack, '_imgBase')}" />
<c:set var="fieldRef" value="${base}${path}"/>

<c:url  var="url" value="${uploadUrl}" />
<c:set var="parentId" value="${f:peek(stack, '_parentId')}"/>
<c:set var="id" value="${f:fid(parentId, path)}_file"/>

<div class="col-sm-12 carousel-container">

    <a href="javascript:void(0)" title="${label}" class="fileinput-button" onclick="$('#${id}').click()" >
        <div class="alert alert-danger" role="alert" data-ng-show="${fieldRef} == null || ${fieldRef}.length == 0">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            No image uploaded yet.
        </div>
        <img data-ng-src="${imgBase}{{${fieldRef}}}" data-ng-show="${fieldRef}.length > 0" >

    </a>

    <c:if test="${not empty uploadUrl}" >
        <input id="${id}" type="file" name="image" data-url="${url}" data-spg-upload >
    </c:if>
</div>