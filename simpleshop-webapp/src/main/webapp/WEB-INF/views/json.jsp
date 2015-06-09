<%@ page import="simpleshop.common.JsonConverter"%>

<%@ page contentType="application/json;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%
    String json = new JsonConverter().toJson(request.getAttribute("content"), (String[])request.getAttribute("excludedFields"));
%><%= json %>

