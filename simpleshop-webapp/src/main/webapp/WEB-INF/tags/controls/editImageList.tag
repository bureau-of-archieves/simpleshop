<%-- This control displays a field inline. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="id" required="false" %>
<%-- Explicitly set the upload url. --%>
<%@attribute name="uploadUrl" required="false" %>
<%@attribute name="label" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:if test="${empty id}">
    <c:set var="id" value="${f:fid(parentId, path)}"/>
</c:if>

<comm:peek var="modelName" />
<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<comm:peek var="modelId" />
<comm:peek var="imgBase" />
<c:set var="fieldRef" value="${base}${path}"/>

<c:choose>
    <c:when test="${empty uploadUrl}" >
        <c:url var="url" value="/upload/${f:pascalToUrl(modelName)}/${modelId}/${f:camelToUrl(path)}" />
    </c:when>
    <c:otherwise>
        <c:url  var="url" value="${uploadUrl}" />
    </c:otherwise>
</c:choose>

<div class="form-group edit-image-list">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>
    <div class="col-sm-9">
        <input id="${id}" type="file" accept="image/*" name="images" data-url="${url}" multiple data-spg-upload-list="${fieldRef}" >

        <ol data-spg-removable-collection="${fieldRef}" class="image-list">
            <li data-ng-repeat="item in ${fieldRef}" >
                <spring:message var="literal_removeImage" code="jsp.literal.removeImage" />
               <div ><span class="glyphicon glyphicon-remove" data-ng-click="remove(item)" title="${literal_removeImage}"></span></div>
                <img data-ng-src="${imgBase}{{item}}" >
            </li>
        </ol>
    </div>
</div>