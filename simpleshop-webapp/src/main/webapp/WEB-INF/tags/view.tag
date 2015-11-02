<%-- All views are wrapped in this tag. It provides view specific stack values. --%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%--########################## TAG CONTENT  ################################--%>
<%--the model type name in space-separated, capitailised words, e.g. Member Account--%>
<comm:push var="friendlyModelName" value="${f:friendlyModelNameFromUrl(pageContext.request.requestURL)}" />
<%--the model name--%>
<comm:push var="modelName" value="${f:friendlyToPascal(friendlyModelName)}" />
<%--view type, e.g. search, details, list, create, update--%>
<comm:push var="viewType" value="${f:viewTypeFromUrl(pageContext.request.requestURL)}" />
<comm:push var="fieldCss" value="col-sm-6" />
<comm:push var="labelCss" value="col-sm-5" />
<comm:push var="detailsCss" value="col-sm-7" />

<jsp:doBody/>

<%--########################## Embed view backing object in json  ################################--%>
<c:set var="modelJsonUrl" value="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}" />
<c:choose>
    <c:when test="${f:isSubViewTypeOf(viewType, 'search') or f:isSubViewTypeOf(viewType, 'list')}">
        <c:set var="dataUrl" value="${modelJsonUrl}/search" />
    </c:when>
    <c:when test="${f:isSubViewTypeOf(viewType, 'create')}">
        <c:set var="dataUrl" value="${modelJsonUrl}/new" />
    </c:when>
    <c:when test="${f:isSubViewTypeOf(viewType, 'details') or f:isSubViewTypeOf(viewType, 'update')}">
        <c:set var="dataUrl" value="${modelJsonUrl}/${param.modelId}" />
    </c:when>
</c:choose>

<c:if test="${not empty dataUrl}">
    <script id="embeddedData">
        <c:catch var="importException" >
            <c:import url="${dataUrl}" />
        </c:catch>
        ${f:throwIfNotNull(importException)}
    </script>
</c:if>

<comm:pop var="fieldCss" />
<comm:pop var="labelCss" />
<comm:pop var="detailsCss" />
<comm:pop var="friendlyModelName" />
<comm:pop var="modelName" />
<comm:pop var="viewType" />
