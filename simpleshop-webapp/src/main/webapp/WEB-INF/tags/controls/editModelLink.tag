<%-- A control used to select an existing model. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="d" tagdir="/WEB-INF/tags/domain" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%--path to the property to set in the base object--%>
<%@attribute name="path" required="true" %>
<%--model name of items in the list--%>
<%@attribute name="targetModelName" required="false" %>
<%--label of this property--%>
<%@attribute name="label" required="false" %>
<%--how to convert the js value object to a string for display--%>
<%@attribute name="displayFormat" required="false" %>
<%--This is used to construct the input element id.--%>
<%@attribute name="parentId" required="false" %>
<%--model name of the object owning this property--%>
<%@attribute name="modelName" required="false" %>
<%--the path used to reference this property in angular--%>
<%@attribute name="base" required="false" %>
<%@attribute name="showLink" type="java.lang.Boolean" required="false" %>
<%--A function to call to convert this field before posting back to server--%>
<%@attribute name="prePostProcessor" type="java.lang.String" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="modelName" value="${modelName}" />

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />

<c:if test="${empty targetModelName}">
    <c:set var="targetModelName" value="${f:propertyTargetModelName(propertyMetadata)}" />
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}" />
</c:if>

<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:set var="id" value="${f:fid(parentId, path)}" />

<comm:peekIfEmpty var="base" value="${base}" />

<%--the angular expression we use to reference the javascript variable/property to assign the selected value to.---%>
<c:set var="fieldRef" value="${base}${path}" />

<c:if test="${empty showLink}" >
    <c:set var="showLink" value="${false}"  />
</c:if>

<c:if test="${empty displayFormat}">
    <c:set var="displayFormat" value="${f:smd(targetModelName).displayFormat}" />
</c:if>
<c:set var="displayFormat" value="${f:combineDisplayFormat(propertyMetadata, displayFormat)}"/>

<c:set var="required" value="${f:fmd(modelName, path).required}"/>

<%--########################## TAG CONTENT ################################--%>
<%--model update is totally controlled by data-spg-combo directive--%>
<c:set var="inputValueExpression" value="${fieldRef}" />
<c:if test="${not empty displayFormat}">
    <c:set var="inputValueExpression" value="${inputValueExpression} | ${f:htmlEncodeQuote(displayFormat)}" />
</c:if>
<comm:push value="${path}" var="path" />

<div class="form-group"
     data-ng-model="${fieldRef}"
     data-name="${path}"
     data-ng-model-options="{updateOn:''}"
     <c:if test="${required}"> data-ng-required="true" </c:if>
     data-spg-combo='{"targetModelName":"${targetModelName}", "displayFormat":"${inputValueExpression}"}'
     <c:if test="${not empty prePostProcessor}">data-pre-post="${prePostProcessor}"</c:if>
>
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <c:set var="showClear" value="${not propertyMetadata.required}" />
    <c:set var="showInputGroup" value="${showLink || showClear}" />
    <div class="col-sm-9">
        <c:if test="${showInputGroup}">
        <div class="input-group">
        </c:if>
            <input id="${id}" type="text" class="form-control" autocomplete="off" name="${path}">

            <c:if test="${showLink}">
            <span class="input-group-addon glyphicon glyphicon-share-alt cursor-pointer" title="<spring:message code="jsp.literal.openInDetailsView" />" data-spg-details='{"modelName":"${targetModelName}","modelId":"#${id}"}'></span>
            </c:if>

            <c:if test="${showClear}">
                <span class="input-group-addon glyphicon glyphicon-remove cursor-pointer" title="<spring:message code="jsp.literal.clearSelection" />" data-ng-click="clearView('${fieldRef}')"></span>
            </c:if>
        <c:if test="${showInputGroup}">
        </div>
        </c:if>

        <div class="combo-list ng-hide" data-ng-show="showList" style="position:relative;" >
            <ol class="list-group hide-children" style="background-color: #fff; position: absolute; z-index: 100; width:100%; top: 0.1em; list-style-type: none">
                <li data-ng-show="loadingList" class="list-group-item display" > <img  style="width:5em;" src="${pageContext.request.contextPath}img/loading.gif" alt="Loading list..."></li>
                <li data-ng-repeat="comboItem in comboList" class="list-group-item display {{$index == selectedIndex ? 'active' : ''}}" data-ng-click="updateView(comboItem)">
                    <span>{{comboItem <c:if test="${not empty displayFormat}"> | ${displayFormat} </c:if> }}</span>
                </li>
                <li class="display no-display-predecessor">No result found.</li>
            </ol>
        </div>

        <ctrl:fieldErrorGroup>
            <c:if test="${required}"><p data-ng-show="${formItemRef}.$error.required"><spring:message code="editField.required" arguments="${path}" /></p></c:if>
        </ctrl:fieldErrorGroup>
    </div>
</div>
<comm:pop var="path" />