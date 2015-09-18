<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="targetModelName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="displayFormat" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="id" required="false" %>
<%@attribute name="itemName" required="false" %>
<%@attribute name="emptyStringAllowed" required="false" type="java.lang.Boolean" %>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>
<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>
<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />
<c:if test="${empty targetModelName}">
    <c:set var="targetModelName" value="${f:collectionModelName(propertyMetadata)}" />
</c:if>
<c:if test="${empty displayFormat}">
    <c:set var="displayFormat" value="${f:smd(targetModelName).displayFormat}" />
</c:if>
<c:set var="displayFormat" value="${f:combineDisplayFormat(propertyMetadata, displayFormat)}"/>
<c:if test="${empty parentId}">
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}"/>
</c:if>
<c:if test="${empty itemName}">
    <c:set var="itemName" value="item"/>
</c:if>
<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>
<c:if test="${empty id}">
    <c:set var="id" value="${f:fid(parentId, path)}"/>
</c:if>
<c:if test="${empty emptyStringAllowed}">
    <c:set var="emptyStringAllowed" value="false"/>
</c:if>

<%--field metadata--%>
<c:set var="propertyType" value="${f:fmd(modelName, path).propertyType}"/>
<c:set var="minLength" value="${f:fmd(modelName, path).minLength}"/>
<c:set var="maxLength" value="${f:fmd(modelName, path).maxLength}"/>
<c:set var="required" value="${f:fmd(modelName, path).required}"/>
<c:set var="min" value="${f:fmd(modelName, path).min}"/>
<c:set var="max" value="${f:fmd(modelName, path).max}"/>
<c:set var="pattern" value="${f:fmd(modelName, path).inputFormat}"/>
<c:set var="displayFormat" value="${f:combineDisplayFormat(f:fmd(modelName, path), displayFormat)}"/>
<c:set var="dateTimeFormat" value="${f:getDateFormatString(displayFormat, propertyType)}" />

<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9 inline-list edit-inline-list">

        <%--<c:set var="fieldRef" value="this['${parentId}-form']['${path}']"/>--%>
        <c:set var="fieldRef" value="${base}${path}"/>

        <select id="${id}"  class="form-control" data-ng-init="dropdownList=[];" data-spg-select='{"modelName":"${targetModelName}", "collectionPath":"${fieldRef}"}' >
            <option data-ng-repeat="item in dropdownList" value="{{$index}}" ng-attr-selected="$index == 0 || undefined">{{item <c:if test="${not empty displayFormat}"> | ${displayFormat} </c:if>}}</option>
        </select>

        <div class="clearfix items-container">
            <span class="pull-left" data-ng-repeat="${itemName} in ${fieldRef}">
                <jsp:doBody/>
            </span>
            <span data-ng-show="${fieldRef}.length == 0" >NONE</span>

        </div>

    </div>
</div>