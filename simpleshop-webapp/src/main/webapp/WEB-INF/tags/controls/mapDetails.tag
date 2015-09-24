<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
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

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peek var="base" />
<comm:peek var="modelName" />

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:if test="${empty displayFormat}">
    <c:set var="displayFormat" value="${f:fmd(modelName, path).displayFormat}"/>
</c:if>

<c:set var="fieldRef" value="${base}${path}"/>

<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-show='${fieldRef}'"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<div ${hideEmptyExpr} class="col-sm-6 details-field">
    <div class="row">
        <div class="col-sm-12">
            <label>${label}</label>
        </div>
    </div>
    <div class="row" data-ng-repeat="(key, value) in ${fieldRef}" >
        <div class="col-sm-5"><span>{{key}}</span></div>
        <div class="col-sm-7"><span>{{value}}</span></div>
    </div>

    <div class="row" data-ng-show="mapSize(${fieldRef}) == 0" >
        <div class="col-sm-5"><span><em><spring:message code="jsp.literal.none" /></em></span></div>
        <div class="col-sm-7"><span></span></div>
    </div>
</div>