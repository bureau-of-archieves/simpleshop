<%@ tag import="simpleshop.domain.model.type.ContactNumberType" %>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="d" tagdir="/WEB-INF/tags/domain" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@attribute name="path" required="true" %>

<%--override model type of the container--%>
<comm:peek var="base" />
<comm:push value="${base}${path}." var="base" />
<comm:push value="Contact" var="modelName" />

<jsp:useBean id="stack" class="simpleshop.webapp.util.ViewValueStackBean" scope="request" />
<%
    stack.push("enumClass", ContactNumberType.class);  //cannot pass class directly in EL
%>

<ctrl:editField path="name" />
<ctrl:editField path="contactName" />
<d:address-form path="address" />
<ctrl:editMap path="contactNumbers" dataListEnum="${stack.pop('enumClass')}"/>
<ctrl:editField path="note" />

<comm:pop var="base" />
<comm:pop var="modelName" />