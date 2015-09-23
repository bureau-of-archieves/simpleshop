
<%@taglib prefix="test" tagdir="/WEB-INF/tags/test" %>
<%@taglib prefix="comm" tagdir="/WEB-INF/tags/common" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<jsp:useBean id="stack" class="simpleshop.webapp.util.ViewValueStackBean" scope="request" />

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP Test Page</title>
</head>
<body>
<comm:push var="test1" value="This is my test."/>
<p>aaa${test1}</p>
<p>${f:peek(stack, "_test1")}</p>
<comm:pop var="test1"/>
<p>bbb${test1}</p>

<%--
How to
http://www.javabeat.net/dynamic-attributes-in-tag-file-in-jsp-2-0/
--%>
<div>
    Server Version: <%= application.getServerInfo() %><br>
    Servlet Version: <%= application.getMajorVersion() %>.<%= application.getMinorVersion() %> <br>
    JSP Version: <%= JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion() %>
</div>

 <h3>${pageContext.ELContext} -> ${pageContext.ELContext}</h3>

<test:test test="#{test1}" />
<test:test test="#{test1}" />
${test1}


<hr>
<test:reqestAttributes/>
</body>
</html>
