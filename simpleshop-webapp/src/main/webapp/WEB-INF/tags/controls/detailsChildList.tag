<%@include file="../_header.tag" %>
<%@attribute name="path" required="true" %>
<%@attribute name="childModelName" required="true" %><%--this is required because the collection type information is lost in runtime--%>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="text" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="itemName" required="false" %>


<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>

<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>

<c:if test="${empty text}">
    <c:set var="text" value="${f:fmd(modelName, path).label}" />
</c:if>

<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>

<c:if test="${empty itemName}" >
    <c:set var="itemName" value="item" />
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}" />

<c:set var="fieldRef" value="${base}${path}" />


<div class="panel panel-default margin-top-1em col-sm-12 details-child-list" >
    <div class="panel-heading">${text} &nbsp;
        <span class="badge">{{${fieldRef}.length}}</span>
    </div>
    <div class="panel-body">

        <div class="panel panel-default" data-ng-repeat="${itemName} in ${fieldRef}">
            <div class="panel-body">
                <dl class="dl-horizontal">
                   ${f:emptyStr(f:push(stack, "_base", f:concat(itemName, '.')))}
                   ${f:emptyStr(f:push(stack, "_modelName", childModelName))}
                   ${f:emptyStr(f:push(stack, "_parentId", id))}
                   <jsp:doBody/>
                   ${f:emptyStr(f:pop(stack, "_base"))}
                   ${f:emptyStr(f:pop(stack, "_modelName"))}
                   ${f:emptyStr(f:pop(stack, "_parentId"))}
                </dl>
            </div>
        </div>

        <div data-ng-hide="{{${fieldRef}.length}}">
            The list is empty.
        </div>

    </div>
</div>