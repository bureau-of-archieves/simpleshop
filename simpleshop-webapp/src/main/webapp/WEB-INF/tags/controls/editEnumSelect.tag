<%-- A control used to set enum type property. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="parentId" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}" />
<c:if test="${empty label}">
    <c:set var="label" value="${propertyMetadata.label}" />
</c:if>

<%--########################## TAG CONTENT ################################--%>
<c:set var="id" value="${f:fid(parentId, path)}" />
<div class="form-group">
    <label for="${id}" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <select id="${id}" name="${path}" class="form-control" data-ng-model="${base}${path}" >
            <c:forEach var="pair" items="${f:options(propertyMetadata.returnType)}">
               <option value="${pair.key}" >${pair.value}</option>
            </c:forEach>
        </select>
    </div>
</div>