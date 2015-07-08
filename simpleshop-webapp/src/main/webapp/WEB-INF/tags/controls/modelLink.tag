<%@include file="../_header.tag" %>

<%@attribute name="path" required="true" %>
<%@attribute name="targetModelName" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="descExpr" %>

<c:if test="${base == null}" >
    <c:set var="base" value="${f:peek(stack, '_base')}" />
</c:if>

<c:set var="idPath" value="${base}${path}.${targetModelName}Id"/>

<a href="javascript:void(0);"
   data-spg-details='{"modelName":"${targetModelName}","modelId":"{{${idPath}}}"}'>
    {{${idPath}}}
    <c:if test="${not empty descExpr}">
        - {{${descExpr}}}
    </c:if>
</a>

