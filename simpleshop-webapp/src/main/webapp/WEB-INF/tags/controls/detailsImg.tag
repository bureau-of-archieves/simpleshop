<%-- An image control that supports upload. //todo implement upload permission in Spring security. --%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="parentId" required="false" %>
<%-- Sets the upload url if image upload should be supported. --%>
<%@attribute name="uploadUrl" required="false" %>
<%@attribute name="label" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />
<c:if test="${not empty uploadUrl}" >
    <c:url  var="url" value="${uploadUrl}" />
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<comm:peek var="imgBase" />
<c:set var="fieldRef" value="${base}${path}"/>
<c:set var="id" value="${f:fid(parentId, path)}_file"/>
<div class="col-sm-12 carousel-container">

    <a href="javascript:void(0)" title="${label}" class="fileinput-button" onclick="$('#${id}').click()" >
        <div class="alert alert-danger" role="alert" data-ng-show="${fieldRef} == null || ${fieldRef}.length == 0">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <spring:message code="jsp.literal.noImageUploadedYet" />
        </div>
        <img data-ng-src="${imgBase}{{${fieldRef}}}" data-ng-show="${fieldRef}.length > 0" >
    </a>

    <c:if test="${not empty uploadUrl}" >
        <input id="${id}" type="file" name="image" data-url="${url}" data-spg-upload >
    </c:if>
</div>