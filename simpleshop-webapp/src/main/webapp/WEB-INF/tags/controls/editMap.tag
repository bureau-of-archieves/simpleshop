<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
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

<div id="${id}" class="form-group">
    <label class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">

        <div class="input-group">
            <input type="text" class="form-control add_entry" aria-label="Add" data-spg-enter-to-click="span.glyphicon-plus">
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-plus cursor-pointer"
                      data-ng-click="addToMap(${fieldRef}, '#${id}')"></span>
            </span>
        </div>

        <hr>

        <div class="row entries">
            <div class="col-sm-12">

                <div class="form-group" data-ng-repeat="(key, value) in ${fieldRef}">
                    <label class="col-sm-4 control-label">{{key}}</label>

                    <div class="col-sm-8">
                        <div class="input-group">
                            <input type="text" class="form-control" data-ng-model="${fieldRef}[key]"
                                   aria-label="Set Value" data-key="{{key}}">
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-minus cursor-pointer"
                                      data-ng-click="removeFromMap(${fieldRef}, key, '#${id}')"></span>
                            </span>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>


</div>