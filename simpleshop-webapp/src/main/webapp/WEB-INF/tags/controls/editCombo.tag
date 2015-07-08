<%@include file="../_header.tag"%>

<%@attribute name="path" required="true" %>
<%@attribute name="targetFriendlyModelName" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="descExpr" required="true" %>

<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>

<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>

<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}" />

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}" />
</c:if>

<c:set var="fieldRef" value="${base}${path}" />
<c:set var="targetModelName" value="${f:friendlyToCamel(targetFriendlyModelName)}"/>
<c:set var="displayFormat" value="${f:htmlEncodeSingleQuote(fn:replace(descExpr, '{0}', fieldRef))}"/>

<%--model update is totally controlled by data-spg-combo directive--%>
<div class="form-group" data-ng-model="${fieldRef}" data-ng-model-options="{updateOn:''}" data-spg-combo='{"targetModelName":"${targetModelName}", "displayFormat":"${displayFormat}"}' >
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <div class="input-group">
            <input id="${id}" type="label" class="form-control" autocomplete="off" name="${path}" >
            <span class="input-group-addon glyphicon glyphicon-share-alt cursor-pointer" title="Open in details view" data-spg-details='{"modelName":"${targetModelName}","modelId":"#${id}"}'></span>
        </div>
        <div class="combo-list" data-ng-show="showList" style="position:relative; " >
            <br>
            <br>
            <img  data-ng-show="loadingList" style="width:1.5em; height:1.5em;" src="${pageContext.request.contextPath}img/loading.gif" alt="Loading list...">
            <ol  class="list-group hide-children" style="background-color: #fff; position: absolute; z-index: 100; width:100%; top: 0.1em;">
                <li data-ng-repeat="comboItem in comboList" class="list-group-item display" data-ng-click="updateView(comboItem)"><a href="javascript:void(0);">{{${fn:replace(descExpr, '{0}', 'comboItem')}}}</a></li>
                <li class="display no-display-predecessor">No result found.</li>
            </ol>
        </div>
    </div>

</div>