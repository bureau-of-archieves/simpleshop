<%--Given a certain amount of indentation by placing invisible 'M' in the span.--%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>

<%@attribute name="source" required="true" %>
<%@attribute name="pattern" required="true"  %>
<%@attribute name="delta" required="true" type="java.lang.Integer"  %>

<span style="opacity: 0">{{${source}|indent:'${pattern}':${delta}}}</span>
