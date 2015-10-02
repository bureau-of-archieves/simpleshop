<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@variable name-given="username" scope="NESTED" %>
<c:if test="${not empty pageContext.request.userPrincipal}">
    <c:set var="username" value="${pageContext.request.userPrincipal.name}" />
    <jsp:doBody/>

</c:if>