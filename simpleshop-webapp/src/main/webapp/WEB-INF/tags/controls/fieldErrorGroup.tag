<%-- A div to put under an input field to show validation errors --%>
<%@tag trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="parentId"%>
<%@attribute name="path"%>
<%@variable name-given="formItemRef" scope="NESTED" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="parentId" value="${parentId}" />
<comm:peekIfEmpty var="path" value="${path}" />

<%--########################## TAG CONTENT ################################--%>
<c:set var="formItemRef" value="this['${parentId}-form']['${path}']"/>
<div class="errors" data-ng-show="${formItemRef}.$dirty && ${formItemRef}.$invalid || showError">
    <jsp:doBody />
</div>