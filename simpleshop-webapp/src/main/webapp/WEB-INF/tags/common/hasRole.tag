<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="role" required="true" %>
<%@attribute name="isFalse" fragment="true" required="false" %>

<c:choose>
    <c:when test="${pageContext.request.isUserInRole(role)}" >
        <jsp:doBody/>
    </c:when>
    <c:otherwise>
        <jsp:invoke fragment="isFalse" />
    </c:otherwise>
</c:choose>