<%-- A select control which loads a list of items from a specified url and bind it to ng-options directive. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="optionsUrl" required="true" %>
<%-- Content of the ng-options directive. By default fetch the items from a scope variable called 'items'. --%>
<%@attribute name="optionsExpression" required="true" %>
<%@attribute name="itemsVariableName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:if test="${empty itemsVariableName}" >
    <c:set var="itemsVariableName" value="items" />
</c:if>

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />
<c:if test="${empty label}">
    <c:set var="label" value="${propertyMetadata.label}" />
</c:if>


<%--########################## TAG CONTENT ################################--%>
<c:set var="id" value="${f:fid(parentId, path)}" />
<c:url var="url" value="${optionsUrl}" />
<c:set var="fieldRef" value="${base}${path}" />
<c:set var="required" value="${propertyMetadata.required}"/>

<comm:push value="${path}" var="path" />
<div class="form-group" data-spg-load-list="${itemsVariableName},${url}">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <select id="${id}" name="${path}" class="form-control"
                data-ng-model="${fieldRef}"
                data-ng-options="${optionsExpression}"
                <c:if test="${required}"> data-ng-required="true" </c:if>>
        </select>

        <ctrl:fieldErrorGroup>
            <c:if test="${required}"><p data-ng-show="${formItemRef}.$error.required"><spring:message code="editField.required" arguments="${path}" /></p></c:if>
        </ctrl:fieldErrorGroup>
    </div>
</div>
<comm:pop var="path" />