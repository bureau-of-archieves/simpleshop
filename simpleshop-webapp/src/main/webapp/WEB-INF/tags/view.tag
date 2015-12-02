<%-- All views are wrapped in this tag. It provides view specific stack values. --%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


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

<comm:pop var="fieldCss" />
<comm:pop var="labelCss" />
<comm:pop var="detailsCss" />
<comm:pop var="friendlyModelName" />
<comm:pop var="modelName" />
<comm:pop var="viewType" />
