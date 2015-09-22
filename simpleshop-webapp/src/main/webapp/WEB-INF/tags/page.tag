<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%--
    The root tag that all jsp pages should use to wrap all content
    All global config are set here.
--%>

<comm:push var="imgBase" value="${pageContext.request.contextPath}assets/img/"  />
<comm:push var="base" value="model."  />

<jsp:doBody/>

<comm:pop var="imgBase" />
<comm:pop var="base" />

<comm:replace-id-marker />



