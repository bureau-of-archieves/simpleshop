<%-- All views are wrapped in this tag. It provides view specific stack values. --%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>

<%--########################## TAG CONTENT  ################################--%>
<%--the model type name in space-separated, capitailised words, e.g. Member Account--%>
<comm:push var="friendlyModelName" value="${f:friendlyModelNameFromUrl(pageContext.request.requestURL)}" />
<%--the model name--%>
<comm:push var="modelName" value="${f:friendlyToPascal(friendlyModelName)}" />
<%--view type, e.g. search, details, list, create, update--%>
<comm:push var="viewType" value="${f:viewTypeFromUrl(pageContext.request.requestURL)}" />

<jsp:doBody/>

<comm:pop var="friendlyModelName" />
<comm:pop var="modelName" />
<comm:pop var="viewType" />
