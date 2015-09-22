<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="displayFormat" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="id" required="false" %>
<%@attribute name="emptyStringAllowed" required="false" type="java.lang.Boolean" %>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>
<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>
<c:if test="${empty parentId}">
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}"/>
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

<c:set var="fieldRef" value="${base}${path}"/>

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

    <div class="col-sm-9">

        <c:if test="${f:isDateTimeType(propertyType)}">
        <div class='input-group date'>
            </c:if>

            <input id="${id}" type="text" class="form-control" name="${path}"
                data-ng-model="${fieldRef}"
                <c:if test="${f:isDateTimeType(propertyType)}"> data-spg-date="${dateTimeFormat}" placeholder="${dateTimeFormat}" </c:if>
                <c:if test="${not emptyStringAllowed}"> data-spg-no-empty-string </c:if>
                <c:if test="${not empty minLength}"> data-ng-minlength="${minLength}" </c:if>
                <c:if test="${not empty maxLength}"> data-ng-maxlength="${maxLength}" </c:if>
                <c:if test="${required}"> data-ng-required="true" </c:if>
                <c:if test="${not empty pattern}"> data-ng-pattern="/${pattern}/" </c:if>
                <c:if test="${min != null}"> data-spg-min="${min}" </c:if>
                <c:if test="${max != null}"> data-spg-max="${max}" </c:if>
             >

            <c:if test="${f:isDateTimeType(propertyType)}">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-time"></span>
                    </span>
        </div>
        </c:if>

        <c:set var="formItemRef" value="this['${parentId}-form']['${path}']"/>
        <div class="errors" data-ng-show="${formItemRef}.$dirty && ${formItemRef}.$invalid || showError">
            <c:if test="${not empty minLength}"><p data-ng-show="${formItemRef}.$error.minlength">Min length is ${minLength}</p></c:if>
            <c:if test="${not empty maxLength}"><p data-ng-show="${formItemRef}.$error.maxlength">Max length is ${maxLength}</p></c:if>
            <c:if test="${not empty pattern}"><p data-ng-show="${formItemRef}.$error.pattern">Not a valid ${propertyType}. Pattern should be "<c:out value="${pattern}"/>".</p></c:if>
            <c:if test="${required}"><p data-ng-show="${formItemRef}.$error.required"><spring:message code="editField.required" arguments="${path}" /></p></c:if>
            <c:if test="${min != null}"><p data-ng-show="${formItemRef}.$error.min"><spring:message code="editField.min" arguments="${path},${min}" /> </p></c:if>
            <c:if test="${max != null}"><p data-ng-show="${formItemRef}.$error.max">Max is ${max}</p></c:if>
        </div>

    </div>
</div>