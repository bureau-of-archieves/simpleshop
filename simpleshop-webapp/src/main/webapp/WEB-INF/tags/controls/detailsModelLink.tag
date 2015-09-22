<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="targetModelName" required="false" %>
<%@attribute name="base" required="false" %>

<c:if test="${base == null}">
    <c:set var="base" value="${f:peek(stack, '_base')}"/>
</c:if>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>
<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}"/>
<c:if test="${empty targetModelName}">
    <c:choose>
        <c:when test="${propertyMetadata.returnTypeMetadata == null}">
            <c:set var="targetModelName" value="${f:camelToPascal(path)}"/>
        </c:when>
        <c:otherwise>
            <c:set var="targetModelName" value="${propertyMetadata.returnTypeMetadata.name}"/>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:set var="fieldRef" value="${base}${path}"/>

<div class="col-sm-6 details-field" data-spg-new-scope>

    <div class="row">
        <div class="col-sm-5"><label> ${label}</label></div>
        <div class="col-sm-7">
            <a href="javascript:void(0);" data-ng-init="targetObject = ${fieldRef}"
               data-spg-details='{"modelName":"${targetModelName}","modelId":"{{${fieldRef}.id}}"}'>

                <jsp:doBody var="body"/>
                ${body}

                <c:if test="${empty body}">
                    <c:set var="displayFormat" value="${f:smd(targetModelName).displayFormat}" />
                    <c:set var="displayFormat" value="${f:combineDisplayFormat(propertyMetadata, displayFormat)}"/>

                    {{targetObject <c:if test="${not empty displayFormat}"> | ${displayFormat} </c:if> }}
                </c:if>
            </a>
        </div>
    </div>
</div>