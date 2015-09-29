<%@ page contentType="application/json;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" import="simpleshop.common.JsonConverter" %>
<%= JsonConverter.getInstance().toJson(request.getAttribute("content"), null) %>

