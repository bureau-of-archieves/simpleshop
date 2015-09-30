<%@ page contentType="application/json;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" import="simpleshop.common.JsonConverter" %><%@ page import="simpleshop.data.infrastructure.SpongeConfigurationException"%>
<% throw new SpongeConfigurationException("This JSP based json view is retired."); %>
<%--<%= JsonConverter.getInstance().toJson(request.getAttribute("content"), null) %>--%>

