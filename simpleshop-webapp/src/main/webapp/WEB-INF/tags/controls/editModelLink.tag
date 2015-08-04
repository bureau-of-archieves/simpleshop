<%@include file="../_header.tag"%>

<%--path to the property to set in the base object--%>
<%@attribute name="path" required="true" %>
<%--model name of items in the list--%>
<%@attribute name="targetModelName" required="false" %>
<%--angular expression used to convert the object to a display string. {0} is the item object.--%>
<%@attribute name="propertyFormatter" required="false" %>
<%--angular expression used to convert the object to a value to bind to the angular model property. {0} is the item object.--%>
<%@attribute name="propertyParser" required="false" %>
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

<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />


<c:if test="${empty targetModelName}">
    <c:set var="targetModelName" value="${f:propertyTargetModelName(propertyMetadata)}" />
</c:if>
<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}" />
</c:if>
<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>
<c:set var="id" value="${f:fid(parentId, path)}" />
<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>
<%--the angular expression we use to reference the javascript variable/property to assign the selected value to.---%>
<c:set var="fieldRef" value="${base}${path}" />
<c:if test="${empty showLink}" >
    <c:set var="showLink" value="false" />
</c:if>

<c:set var="displayFormat" value="${f:combineDisplayFormat(propertyMetadata, displayFormat)}"/>

<%--model update is totally controlled by data-spg-combo directive--%>
<div class="form-group" data-ng-model="${fieldRef}" data-ng-model-options="{updateOn:''}" data-spg-combo='{"targetModelName":"${targetModelName}", "displayFormat":"${fieldRef} ${f:htmlEncodeSingleQuote(displayFormat)}"}' >
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <c:if test="${showLink}">
        <div class="input-group">
        </c:if>
            <input id="${id}" type="text" class="form-control" autocomplete="off" name="${path}" >
        <c:if test="${showLink}">
            <span class="input-group-addon glyphicon glyphicon-share-alt cursor-pointer" title="Open in details view" data-spg-details='{"modelName":"${targetModelName}","modelId":"#${id}"}'></span>
        </div>
        </c:if>
        <div class="combo-list" data-ng-show="showList" style="position:relative;" >

            <ol class="list-group hide-children" style="background-color: #fff; position: absolute; z-index: 100; width:100%; top: 0.1em; list-style-type: none">
                <li data-ng-show="loadingList" class="list-group-item display" > <img  style="width:5em;" src="${pageContext.request.contextPath}img/loading.gif" alt="Loading list..."></li>
                <li data-ng-repeat="comboItem in comboList" class="list-group-item display" data-ng-click="updateView(comboItem)"><a href="javascript:void(0);">
                    {{comboItem ${displayFormat}}}</a>
                </li>
                <li class="display no-display-predecessor">No result found.</li>
            </ol>
        </div>
    </div>

</div>