<%-- This control shows a model collection property as a list of inline elements. --%>
<%@tag trimDirectiveWhitespaces="true"  %>
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
<%@attribute name="path" required="true" %>
<%@attribute name="childModelName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="itemName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="title" required="false" %>
<%@attribute name="fieldCss" required="false" %>
<%@attribute name="labelCss" required="false" %>
<%@attribute name="detailsCss" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />
<comm:peekIfEmpty var="fieldCss" value="${fieldCss}" />
<comm:peekIfEmpty var="labelCss" value="${labelCss}" />
<comm:peekIfEmpty var="detailsCss" value="${detailsCss}" />

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}"/>
<c:if test="${empty childModelName}">
    <c:set var="childModelName" value="${f:collectionModelName(propertyMetadata)}"/>
</c:if>

<c:if test="${empty itemName}">
    <c:set var="itemName" value="item"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<c:set var="id" value="${f:fid(parentId, path)}"/>
<c:set var="fieldRef" value="${base}${path}"/>
<div class="${fieldCss} inline-list details-inline-list">

    <div class="row">
        <div class="${labelCss}"><label> ${label}</label></div>
        <div class="${detailsCss} clearfix"  <c:if test="${not empty title}"> title="${title}" </c:if>>

            <span class="pull-left" data-ng-repeat="${itemName} in ${fieldRef}">
                <jsp:doBody/>
            </span>
            <span data-ng-show="${fieldRef}.length == 0" ><spring:message code="jsp.literal.none" /></span>
        </div>
    </div>
</div>





