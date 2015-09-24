<%-- A control which use a checkbox to update a boolean field.--%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="text" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="trueValue" required="false" %>
<%@attribute name="falseValue" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:if test="${empty text}">
    <c:set var="text" value="${f:fmd(modelName, path).label}" />
</c:if>

<%--########################## TAG CONTENT ################################--%>
<c:set var="id" value="${f:fid(parentId, path)}" />
<div class="form-group">
    <div class="col-sm-offset-3 col-sm-9">
        <div class="checkbox">
            <label>
                <input id="${id}" name="${path}" type="checkbox" data-ng-model="${base}${path}"
                        <c:if test="${not empty trueValue}"> data-ng-true-value="'${trueValue}'"</c:if>
                        <c:if test="${not empty falseValue}"> data-ng-false-value="'${falseValue}'"</c:if>> ${text}
            </label>
        </div>
    </div>
</div>