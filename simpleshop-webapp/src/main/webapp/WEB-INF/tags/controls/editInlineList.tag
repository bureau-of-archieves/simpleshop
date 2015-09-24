<%-- A control that shows a list of model objects where each object is presented as an inline element. --%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="targetModelName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="displayFormat" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="id" required="false" %>
<%@attribute name="itemName" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="base" value="${base}" />

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />
<c:if test="${empty targetModelName}">
    <c:set var="targetModelName" value="${f:collectionModelName(propertyMetadata)}" />
</c:if>

<c:if test="${empty displayFormat}">
    <c:set var="displayFormat" value="${f:smd(targetModelName).displayFormat}" />
</c:if>
<c:set var="displayFormat" value="${f:combineDisplayFormat(propertyMetadata, displayFormat)}"/>

<c:if test="${empty itemName}">
    <c:set var="itemName" value="item"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<comm:peekIfEmpty var="parentId" value="${parentId}" />
<c:if test="${empty id}">
    <c:set var="id" value="${f:fid(parentId, path)}"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<c:set var="displayFormat" value="${f:combineDisplayFormat(f:fmd(modelName, path), displayFormat)}"/>
<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <c:set var="fieldRef" value="${base}${path}"/>
    <div class="col-sm-9 inline-list edit-inline-list">
        <select id="${id}"  class="form-control" data-ng-init="dropdownList=[];" data-spg-select='{"modelName":"${targetModelName}", "collectionPath":"${fieldRef}"}' >
            <option data-ng-repeat="item in dropdownList" value="{{$index}}" data-ng-click="add(item)" data-ng-attr-selected="$index == 0 || undefined">{{item <c:if test="${not empty displayFormat}"> | ${displayFormat} </c:if>}}</option>
        </select>

        <div class="clearfix items-container" data-spg-removable-collection="${fieldRef}">
            <span class="pull-left" data-ng-repeat="${itemName} in ${fieldRef}">
                <jsp:doBody/>
            </span>
            <span data-ng-show="${fieldRef}.length == 0" ><spring:message code="jsp.literal.none" /></span>
        </div>

    </div>
</div>