<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="displayFormat" required="false" %>
<%@attribute name="hideEmpty" type="java.lang.Boolean" required="false" %>
<%@attribute name="title" required="false" %>

<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:set var="displayFormat" value="${f:combineDisplayFormat(f:fmd(modelName, path), displayFormat)}"/>

<c:set var="fieldRef" value="${base}${path}"/>

<c:set var="hideEmptyExpr" value=""/>
<c:if test="${hideEmpty}">
    <c:set var="hideEmptyExpr" value="data-ng-show='${fieldRef}'"/>
</c:if>

<div ${hideEmptyExpr} class="col-sm-6 details-field">

    <div class="row">
        <div class="col-sm-5"><label> ${label}</label></div>
        <div class="col-sm-7"  <c:if test="${not empty title}"> title="${title}" </c:if>>
            <c:choose>
                <c:when test="${displayFormat == 'url'}">
                    <a target="_blank" href="{{${fieldRef} | url}}">{{${fieldRef} | url_label}}</a>
                </c:when>
                <c:otherwise>
                    {{${fieldRef} <c:if test="${not empty displayFormat}">| ${displayFormat}</c:if>}}
                </c:otherwise>
            </c:choose>

            <jsp:doBody/>
        </div>
    </div>
</div>