<%-- A control which allows edit of a model collection property. --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="path" required="true" %>
<%@attribute name="childModelName" required="false" %>
<%@attribute name="base" required="false" %>
<%@attribute name="modelName" required="false" %>
<%@attribute name="parentId" required="false" %>
<%@attribute name="label" required="false" %>
<%@attribute name="itemName" required="false" %>
<%@attribute name="readonly" required="false" type="java.lang.Boolean" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="modelName" value="${modelName}" />
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="parentId" value="${parentId}" />

<c:set var="propertyMetadata" value="${f:fmd(modelName, path)}"/>
<c:if test="${empty childModelName}">
    <c:set var="childModelName" value="${f:collectionModelName(propertyMetadata)}"/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<c:if test="${empty itemName}">
    <c:set var="itemName" value="item"/>
</c:if>

<%--########################## TAG CONTENT ################################--%>
<c:set var="id" value="${f:fid(parentId, path)}"/>
<c:set var="fieldRef" value="${base}${path}"/>
<div class="form-group ">
    <div class="col-sm-12">

        <div class="panel panel-default margin-bottom-0">
            <div class="panel-heading">${label} &nbsp;
                <span class="badge">{{${fieldRef}.length}}</span>
                <span class="glyphicon glyphicon-plus pull-right cursor-pointer" data-ng-click="addToCollection(${fieldRef},'${childModelName}')"></span>
            </div>
            <div class="panel-body" data-ng-show="${fieldRef}.length">

                <div class="panel panel-default" data-ng-repeat="${itemName} in ${fieldRef}" data-ng-form="${id}-form">
                    <div class="panel-body">
                        <dl class="dl-horizontal">
                            <comm:push value="${f:concat(itemName, '.')}" var="base"  />
                            <comm:push value="${childModelName}" var="modelName"  />
                            <comm:push value="${id}" var="parentId"  />
                            <jsp:doBody/>
                            <comm:pop var="base" />
                            <comm:pop var="modelName" />
                            <comm:pop var="parentId" />
                        </dl>
                        <c:if test="${not readonly}" >
                            <button type="submit" class="btn btn-primary" data-ng-click="removeFromCollection(${fieldRef}, ${itemName})">
                                <spring:message code="jsp.literal.remove" />
                            </button>
                        </c:if>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>