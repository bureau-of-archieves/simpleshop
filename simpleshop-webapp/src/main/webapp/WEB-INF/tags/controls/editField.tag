<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="id" required="false" %>


<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>
<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>
<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>
<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}" />
</c:if>
<c:if test="${empty id}">
    <c:set var="id" value="${f:fid(parentId, path)}" />
</c:if>


<%--field metadata--%>
<c:set var="propertyType" value="${f:fmd(modelName, path).propertyType}" />
<c:set var="minLength" value="${f:fmd(modelName, path).minLength}" />
<c:set var="maxLength" value="${f:fmd(modelName, path).maxLength}" />
<c:set var="required" value="${f:fmd(modelName, path).required}" />
<c:set var="min" value="${f:fmd(modelName, path).min}" />
<c:set var="max" value="${f:fmd(modelName, path).max}" />
<c:set var="pattern" value="${f:fmd(modelName, path).inputFormat}" />

<c:set var="directive" value="" />
<c:if test="${propertyType == 'Date'}">
    <c:set var="directive" value="data-spg-date='yyyy-MM-dd'" />
</c:if>

<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <input id="${id}" type="text" class="form-control" name="${path}"
            data-ng-model="${base}${path}"
            ${directive}
            <c:if test="${not empty minLength}"> data-ng-minlength="${minLength}" </c:if>
            <c:if test="${not empty maxLength}"> data-ng-maxlength="${maxLength}" </c:if>
            <c:if test="${required}"> data-ng-required="true" </c:if>
            <c:if test="${not empty pattern}"> data-ng-pattern="/${pattern}/" </c:if>
            <c:if test="${min != null}"> data-spg-min="${min}" </c:if>
            <c:if test="${max != null}"> data-spg-max="${max}" </c:if> >

        <c:set var="fieldRef" value="this['${parentId}-form']['${path}']" />
        <div class="errors" data-ng-show="${fieldRef}.$dirty && ${fieldRef}.$invalid">
            <c:if test="${not empty minLength}"> <p data-ng-show="${fieldRef}.$error.minlength">Min length is ${minLength}</p> </c:if>
            <c:if test="${not empty maxLength}"> <p data-ng-show="${fieldRef}.$error.maxlength">Max length is ${maxLength}</p> </c:if>
            <c:if test="${required}"> <p data-ng-show="${fieldRef}.$error.required">Required</p> </c:if>
            <c:if test="${not empty pattern}">
            <c:choose>
                <c:when test="${propertyType == 'BigDecimal'}">
                    <p data-ng-show="${fieldRef}.$error.pattern">Not a valid decimal.</p>
                </c:when>
                <c:when test="${propertyType == 'Integer'}">
                    <p data-ng-show="${fieldRef}.$error.pattern">Not a valid integer.</p>
                </c:when>
                <c:when test="${propertyType == 'Date'}">
                    <p data-ng-show="${fieldRef}.$error.pattern">Not a valid date.</p>
                </c:when>
                <c:otherwise>
                    <p data-ng-show="${fieldRef}.$error.pattern">Pattern should be "<c:out value="${pattern}" />".</p>
                </c:otherwise>
            </c:choose>
            </c:if>
            <c:if test="${min != null}"> <p data-ng-show="${fieldRef}.$error.min">Min is ${min}</p> </c:if>
            <c:if test="${max != null}"> <p data-ng-show="${fieldRef}.$error.max">Max is ${max}</p> </c:if>
        </div>

    </div>
</div>