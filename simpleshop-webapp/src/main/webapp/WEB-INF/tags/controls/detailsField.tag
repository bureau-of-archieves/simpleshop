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

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:set var="displayFormat" value="${f:combineDisplayFormat(f:fmd(modelName, path), displayFormat)}"/>

<c:set var="fieldRef" value="${base}${path}"/>
<c:set var="hideEmptyExpr" value=""/>
<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-show='${fieldRef}'"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<div ${hideEmptyExpr} class="col-sm-6 details-field">

    <div class="row">
        <div class="col-sm-5"><label> ${label}</label></div>
        <div class="col-sm-7" <c:if test="${not empty title}"> title="${title}" </c:if>>
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