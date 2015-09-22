<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="code" type="java.lang.String" required="true" %>
<%@attribute name="arguments" %>


<spring:message var="errorMessage" code="${code}" arguments="${arguments}" />
${f:error(errorMessage)}

