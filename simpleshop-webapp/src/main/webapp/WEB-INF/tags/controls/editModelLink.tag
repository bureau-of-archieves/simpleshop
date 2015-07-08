<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="targetFriendlyModelName" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="text" required="false" %>
<%@attribute name="parentId" required="false" %>

<%@attribute name="descExpr" required="true" %>


<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>

<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>

<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>

<c:if test="${empty text}">
    <c:set var="text" value="${f:fmd(modelName, path).label}" />
</c:if>

<c:set var="targetModelName" value="${f:friendlyToCamel(targetFriendlyModelName)}"/>
<c:set var="id" value="${f:fid(parentId, path)}"/>
<c:set var="idProperty" value="${targetModelName}Id"/>


<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${text}</label>

    <div class="col-sm-4 col-lg-3">

        <div class="input-group">
            <div class="input-group-addon" data-spg-begin-link-request="${id}">
                <span class="glyphicon glyphicon-share-alt pull-right cursor-pointer"></span>
            </div>
            <input id="${id}" type="text" class="form-control" name="${path}.${idProperty}"
                   data-ng-model="${base}${path}.${idProperty}"
                   data-model-name="${targetModelName}">

            <r:modelAnchor modelName="${targetModelName}" idExpr="{{${base}${path}.${idProperty}}}" descExpr="{{${base}${path}.${descExpr}}}" addon="true" />
        </div>

    </div>
    <div class="col-sm-5 col-lg-6" style="padding-left: 0">
        <a id="${id}-desc" href="javascript:void(0);" class="field-label" data-ng-click="getById('${targetModelName}', '#${id}')" data-ng-bind="${base}${path}.${descExpr}">
        </a>
    </div>
</div>