<%@include file="WEB-INF/_header.jspf" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<body>
    <comm:push var="test1" value="This is my test."  />
    <p>aaa${test1}</p>
    <p>${f:peek(stack, "_test1")}</p>
    <comm:pop var="test1" />
    <p>bbb${test1}</p>
</body>
</html>
