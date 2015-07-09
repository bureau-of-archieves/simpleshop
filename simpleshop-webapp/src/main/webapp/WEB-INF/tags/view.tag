<%@include file="_header.tag" %>
<%---The common frame view logic of all views in the SPA page.--%>


<%--the model type name in space-separated, capitailised words, e.g. Member Account--%>
<c:set var="friendlyModelName" value="${f:friendlyModelNameFromUrl(pageContext.request.requestURL)}" />
<%--the model name--%>
<c:set var="modelName" value="${f:friendlyToPascal(friendlyModelName)}" />
<%--view type, e.g. search, details, list, create, update--%>
<c:set var="viewType" value="${f:viewTypeFromUrl(pageContext.request.requestURL)}" />

${f:_push(stack, "_friendlyModelName", friendlyModelName)}
${f:_push(stack, "_modelName", modelName)}
${f:_push(stack, "_viewType", viewType)}

<jsp:doBody/>

${f:_pop(stack, "_friendlyModelName")}
${f:_pop(stack, "_modelName")}
${f:_pop(stack, "_viewType")}