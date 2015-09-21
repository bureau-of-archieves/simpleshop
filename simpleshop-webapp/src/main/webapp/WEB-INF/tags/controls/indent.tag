<%@include file="../_header.tag" %>
<%--represents an icon--%>

<%@attribute name="source" required="true" %>
<%@attribute name="pattern" required="true"  %>
<%@attribute name="delta" required="true" type="java.lang.Integer"  %>

<span style="opacity: 0">{{${source}|indent:'${pattern}':${delta}}}</span>
