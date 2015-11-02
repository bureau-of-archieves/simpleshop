<%-- This control displays a field inline. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="displayFormat" required="false" %>
<%@attribute name="hideEmpty" type="java.lang.Boolean" required="false" %>
<%@attribute name="title" required="false" %>
<%@attribute name="fieldCss" required="false" %>
<%@attribute name="labelCss" required="false" %>
<%@attribute name="detailsCss" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />
<c:if test="${empty label}">
    <c:set var="label" value="${propertyMetadata.label}"/>
</c:if>
<comm:peekIfEmpty var="fieldCss" value="${fieldCss}" />
<comm:peekIfEmpty var="labelCss" value="${labelCss}" />
<comm:peekIfEmpty var="detailsCss" value="${detailsCss}" />

<c:set var="displayFormat" value="${f:combineDisplayFormat(propertyMetadata, displayFormat)}"/>

<c:set var="fieldRef" value="${base}${path}"/>
<c:set var="hideEmptyExpr" value=""/>
<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-show='${fieldRef}'"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<div ${hideEmptyExpr} class="${fieldCss} details-field">

    <div class="row">
        <div class="${labelCss}"><label> ${label}</label></div>
        <div class="${detailsCss}" <c:if test="${not empty title}"> title="${title}" </c:if>>
            <c:choose>
                <c:when test="${displayFormat == 'url'}">
                    <a target="_blank" href="{{${fieldRef} | url}}">{{${fieldRef} | url_label}}</a>
                </c:when>
                <c:otherwise>
                    {{${fieldRef} <c:if test="${not empty displayFormat}">| ${displayFormat}</c:if>}}
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>