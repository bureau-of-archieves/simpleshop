<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="childModelName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="itemName" required="false" %>


<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:if test="${empty parentId}">
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}"/>
</c:if>

<c:if test="${empty itemName}">
    <c:set var="itemName" value="item"/>
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}"/>

<c:set var="fieldRef" value="${base}${path}"/>

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}"/>

<c:if test="${empty childModelName}">
    <c:set var="childModelName" value="${f:collectionModelName(propertyMetadata)}"/>
</c:if>

<div class="form-group ">
    <div class="col-sm-offset-3 col-sm-9">

        <div class="panel panel-default margin-bottom-0">
            <div class="panel-heading">${label} &nbsp;
                <span class="badge">{{${fieldRef}.length}}</span>
                <span class="glyphicon glyphicon-plus pull-right cursor-pointer"
                      data-ng-click="addToCollection(${fieldRef},'${childModelName}')"></span>
            </div>
            <div class="panel-body"
                 data-ng-show="${fieldRef}.length">

                <div class="panel panel-default"
                     data-ng-repeat="${itemName} in ${fieldRef}" data-ng-form="${id}-form">
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
                        <button type="submit" class="btn btn-primary"
                                data-ng-click="removeFromCollection(${fieldRef}, ${itemName})">Remove
                        </button>
                    </div>
                </div>


            </div>
        </div>
    </div>
</div>