<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="text" required="false" %>
<%@attribute name="parentId" required="false" %>

<%@attribute name="trueValue" required="false" %>
<%@attribute name="falseValue" required="false" %>


<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>

<c:if test="${empty modelName}" >
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />
</c:if>

<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>

<c:if test="${empty text}">
    <c:set var="text" value="${f:fmd(modelName, path).label}" />
</c:if>

<c:set var="id" value="${f:fid(parentId, path)}" />


<div class="form-group">
    <div class="col-sm-offset-3 col-sm-9">
        <div class="checkbox">
            <label>
                <input id="${id}" name="${path}" type="checkbox" data-ng-model="${base}${path}"
                        <c:if test="${not empty trueValue}"> data-ng-true-value="'${trueValue}'"</c:if>
                        <c:if test="${not empty falseValue}"> data-ng-false-value="'${falseValue}'"</c:if>
                        > ${text}
            </label>
        </div>
    </div>
</div>