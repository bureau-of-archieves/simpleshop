<%-- A select control which loads a list of items from a specified url and bind it to ng-options directive. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>

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

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}" />
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}" />

<c:url var="url" value="${optionsUrl}" />

<c:set var="fieldRef" value="${base}${path}" />

<%--########################## TAG CONTENT ################################--%>
<div class="form-group" data-spg-load-list="${itemsVariableName},${url}">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <select id="${id}" name="${path}" class="form-control" data-ng-model="${fieldRef}" data-ng-options="${optionsExpression}" >
        </select>
    </div>
</div>