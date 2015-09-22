<%@ tag import="java.util.Enumeration"  %>
<%@attribute name="" rtexprvalue="false" %>
<ul>
    <%
        Enumeration names = request.getAttributeNames();
        while (names.hasMoreElements()) {
    %>
    <li><%= names.nextElement() %>
    </li>
    <%
        }
    %>
</ul>