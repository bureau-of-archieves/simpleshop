<%@ page import="simpleshop.common.JsonConverter"%>

<%@ page contentType="application/json;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%
    String json = JsonConverter.getInstance().toJson(request.getAttribute("content"), (String[])request.getAttribute("excludedFields"));
%><%= json %>

