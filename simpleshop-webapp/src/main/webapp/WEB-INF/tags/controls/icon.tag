<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@taglib prefix="comm" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--represents an icon--%>

<%@attribute name="value" required="true" %>
<%@attribute name="type" %>
<%@attribute name="attributes" fragment="true" %>
<%@attribute name="cssClass" %>
<%@attribute name="alignRight" type="java.lang.Boolean" %>
<%@attribute name="title" %>

<c:if test="${empty type}">
    <c:set var="type" value="glyph" />
</c:if>

<c:choose>
    <c:when test="${type == 'glyph'}" >
        <c:set var="cssClass" value="glyphicon glyphicon-${value} ${cssClass}" />
    </c:when>
    <%--todo other icon types, e.g. site--%>
    <c:otherwise>
        <comm:error code="jsp.unsupportedIconType" arguments="${type}" />
    </c:otherwise>
</c:choose>

<c:if test="${alignRight}">
    <c:set var="cssClass" value="pull-right ${cssClass}" />
</c:if>

<span   class="${cssClass}"
        <c:if test="${not empty title}"> title="${title}" </c:if>
        <jsp:invoke fragment="attributes" />></span>
