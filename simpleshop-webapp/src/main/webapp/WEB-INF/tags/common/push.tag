<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<jsp:useBean id="stack" class="simpleshop.webapp.util.ViewValueStackBean" scope="request" />

<%-- push a value onto the the request level value stack of var.--%>
<%@attribute name="value" required="true" %>
<%@attribute name="var" rtexprvalue="false" type="java.lang.String" required="true" %>
<%@ variable alias="result" name-from-attribute="var" scope="AT_END" %>


${f:_push(stack, '_'.concat(var), value)}
<c:set var="result" value="${f:peek(stack,  '_'.concat(var))}" />


