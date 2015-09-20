<%@include file="_header.tag"%>
<%--
    The root tag that all jsp pages should use to wrap all content
    All global config are set here.
--%>

<c:set var="imgBase" value="${pageContext.request.contextPath}assets/img/" />

${f:_push(stack, "_base", "model.")}
${f:_push(stack, "_colPrefix", "col-sm-")}
${f:_push(stack, "_imgBase", imgBase)}

<jsp:doBody/>

${f:_pop(stack, "_base")}
${f:_pop(stack, "_colPrefix")}
${f:_pop(stack, "_imgBase")}
<c:if test="${not empty f:peek(stack, '_replace_id_marker')}" >
    <replace-id-marker>${f:peek(stack, "_replace_id_marker")}</replace-id-marker>
</c:if>


