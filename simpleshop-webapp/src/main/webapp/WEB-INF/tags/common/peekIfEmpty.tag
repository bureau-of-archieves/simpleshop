<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<jsp:useBean id="stack" class="simpleshop.webapp.util.ViewValueStackBean" scope="request" />

<%-- peek the top of the request level value stack of var.--%>
<%@attribute name="var" type="java.lang.String" rtexprvalue="false" required="true" %>
<%@ variable alias="result" name-from-attribute="var" scope="AT_END" %>
<%@attribute name="value" required="true" %>

<c:choose>
    <c:when test="${empty value}">
        <c:set var="result" value="${f:peek(stack, '_'.concat(var))}" />
    </c:when>
    <c:otherwise>
        <c:set var="result" value="${value}" />
    </c:otherwise>
</c:choose>
