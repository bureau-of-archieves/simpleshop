
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag %>
<%@attribute name="test" deferredValue="true" rtexprvalue="false"    %>



<h3>${pageContext} -> ${pageContext.ELContext}</h3>

<div>
 [[${test.getValue(pageContext.ELContext)}]]
</div>


