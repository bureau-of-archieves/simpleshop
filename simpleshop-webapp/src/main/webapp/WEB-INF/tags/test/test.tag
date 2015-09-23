
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag %>
<%@attribute name="test" deferredValue="true" rtexprvalue="false" required="true"   %>

<c:set var="test1" value="HHH" />

<h3>${pageContext} -> ${pageContext.ELContext}</h3>

<div>
 [[${test}]]
</div>
