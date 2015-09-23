<%@tag body-content="empty" %>
<%@ tag import="java.util.Enumeration" %>

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

<h2>${pageContext}</h2>