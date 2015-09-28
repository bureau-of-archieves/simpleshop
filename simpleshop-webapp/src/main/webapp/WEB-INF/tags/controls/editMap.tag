<%-- A control used to update a map field. Only support string to string map at the moment. --%>
<%@tag body-content="empty" trimDirectiveWhitespaces="true" %>
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
<%@attribute name="dataListEnum" required="false" type="java.lang.Class" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="base" value="${base}" />
<comm:peekIfEmpty var="modelName" value="${modelName}" />

<c:if test="${empty label}">
    <c:set var="label" value="${f:fmd(modelName, path).label}"/>
</c:if>

<comm:peekIfEmpty var="parentId" value="${parentId}" />

<%--########################## TAG CONTENT ################################--%>
<c:set var="id" value="${f:fid(parentId, path)}"/>
<c:set var="fieldRef" value="${base}${path}"/>
<div id="${id}" class="form-group">
    <label for="${id}-text" class="col-sm-3 control-label">${label}</label>

    <div class="col-sm-9">
        <div class="input-group">
            <input id="${id}-text" type="text" class="form-control add_entry" aria-label="Add" data-spg-enter-to-click="span.glyphicon-plus"
            <c:if test="${not empty dataListEnum}">
                <c:set var="listId" value="${id}-list" />
                list="${listId}"
            </c:if>
            >
            <c:if test="${not empty dataListEnum}">
                <datalist id="${listId}" >
                    <c:forEach var="pair" items="${f:options(dataListEnum)}">
                        <option value="${pair.value}" >
                    </c:forEach>
                </datalist>
            </c:if>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-plus cursor-pointer"  data-ng-click="addToMap(${fieldRef}, '#${id}')"></span>
            </span>
        </div>

        <hr>

        <div class="row entries">
            <div class="col-sm-12">

                <div class="form-group" data-ng-repeat="(key, value) in ${fieldRef}">
                    <label class="col-sm-4 control-label">{{key}}</label>

                    <div class="col-sm-8">
                        <div class="input-group">
                            <spring:message var="literal_entryValue" code="jsp.literal.setEntryValue" />
                            <spring:message var="literal_setValueFor" code="jsp.literal.setValueFor" />
                            <input type="text" title="${literal_setValueFor} {{key}}" class="form-control" data-ng-model="${fieldRef}[key]" aria-label="${literal_entryValue}" data-key="{{key}}">
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-minus cursor-pointer" data-ng-click="removeFromMap(${fieldRef}, key, '#${id}')"></span>
                            </span>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>


</div>