<%-- A control for updating a text or date field. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="displayFormat" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="id" required="false" %>
<%-- true if this field can accept an empty string --%>
<%@attribute name="emptyStringAllowed" required="false" type="java.lang.Boolean" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:if test="${empty id}">
    <c:set var="id" value="${f:fid(parentId, path)}"/>
</c:if>

<c:if test="${empty emptyStringAllowed}">
    <c:set var="emptyStringAllowed" value="false"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<%--field metadata--%>
<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />
<c:set var="propertyType" value="${propertyMetadata.propertyType}"/>
<c:set var="minLength" value="${propertyMetadata.minLength}"/>
<c:set var="maxLength" value="${propertyMetadata.maxLength}"/>
<c:set var="required" value="${propertyMetadata.required}"/>
<c:set var="min" value="${propertyMetadata.min}"/>
<c:set var="max" value="${propertyMetadata.max}"/>
<c:set var="pattern" value="${propertyMetadata.inputFormat}"/>
<c:set var="displayFormat" value="${f:combineDisplayFormat(propertyMetadata, displayFormat)}"/>

<c:set var="fieldRef" value="${base}${path}"/>
<comm:push value="${path}" var="path" />
<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <c:if test="${f:isDateTimeType(propertyType)}">
        <div class='input-group date'>
        </c:if>
            <input id="${id}" type="text" class="form-control" name="${path}"
                data-ng-model="${fieldRef}"
                <c:if test="${f:isDateTimeType(propertyType)}">
                <c:set var="dateTimeFormat" value="${f:getDateFormatString(displayFormat, propertyType)}" />
                   data-spg-date="${dateTimeFormat}" placeholder="${dateTimeFormat}"
                </c:if>
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

        <ctrl:fieldErrorGroup>
            <c:if test="${not empty minLength}"><p data-ng-show="${formItemRef}.$error.minlength"><spring:message code="editField.minLength" arguments="${path},${minLength}" /></p></c:if>
            <c:if test="${not empty maxLength}"><p data-ng-show="${formItemRef}.$error.maxlength"><spring:message code="editField.maxLength" arguments="${path},${maxLength}" /></p></c:if>
            <c:if test="${not empty pattern}"><p data-ng-show="${formItemRef}.$error.pattern"><spring:message code="editField.pattern" arguments="${path},${propertyType},${f:htmlEncodeQuote(fn:escapeXml(pattern))}" /></p></c:if>
            <c:if test="${required}"><p data-ng-show="${formItemRef}.$error.required"><spring:message code="editField.required" arguments="${path}" /></p></c:if>
            <c:if test="${min != null}"><p data-ng-show="${formItemRef}.$error.min"><spring:message code="editField.min" arguments="${path},${min}" /></p></c:if>
            <c:if test="${max != null}"><p data-ng-show="${formItemRef}.$error.max"><spring:message code="editField.max" arguments="${path},${max}" /></p></c:if>
        </ctrl:fieldErrorGroup>
    </div>
</div>
<comm:pop var="path" />