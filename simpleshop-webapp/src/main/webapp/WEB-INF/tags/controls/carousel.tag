<%-- A carousel control used to display a list of images.--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="hideEmpty" type="java.lang.Boolean" required="false" %>
<%@attribute name="title" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:set var="fieldRef" value="${base}${path}"/>

<c:set var="hideEmptyExpr" value=""/>
<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-hide='${fieldRef} == null || ${fieldRef}.length == 0'"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<comm:peek var="imgBase" />
<div ${hideEmptyExpr} class="col-sm-12 carousel-container spg-carousel" >

    <div class="alert alert-danger ng-hide" role="alert" data-ng-show="${fieldRef} == null || ${fieldRef}.length == 0">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <spring:message code="jsp.literal.noImageUploadedYet" />
    </div>

    <uib-carousel interval="5000" no-wrap="false">
        <uib-slide ng-repeat="slide in ${fieldRef}" >
            <img data-ng-src="${imgBase}{{slide}}" >
            <div class="carousel-caption">
                <p>${label} <c:if test="${not empty title}"> title="${title}" </c:if></p>
            </div>
        </uib-slide>
    </uib-carousel>
</div>