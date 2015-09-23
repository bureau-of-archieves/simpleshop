<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<jsp:useBean id="stack" class="simpleshop.webapp.util.ViewValueStackBean" scope="request" />

<%-- pop the request level value stack of var.--%>
<%@attribute name="var" type="java.lang.String" rtexprvalue="false" required="true" %>
<%@ variable alias="result" name-from-attribute="var" scope="AT_END" %>


<c:set var="result" value="${f:_pop(stack,  '_'.concat(var))}" />


