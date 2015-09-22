<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>

<%--the id of this view, must be unique in the page--%>
<%@attribute name="id" required="true" %>
<%--title text of the frame panel--%>
<%@attribute name="title" type="java.lang.String" required="true" %>
<%--the icon of the panel--%>
<%@attribute name="icon" %>
<%--the name of the ng controller of this view, which does not include the suffix 'Controller'.--%>
<%@attribute name="controllerName" %>
<%--the model name, which determines which ClassMetadata is to use with this view--%>
<%@attribute name="modelName" %>
<%--how many columns this view spans, from 1 to 12.--%>
<%@attribute name="col" type="java.lang.Integer"  %>
<%--content that goes to the frame panel header--%>
<%@attribute name="header" fragment="true" %>
<%--the css class for the panel--%>
<%@attribute name="cssClass" %>
<%--bootstrap panel css class--%>
<%@attribute name="panelClass" %>
<%--bootstrap view frame css class--%>
<%@attribute name="frameClass" %>
<%--just hide this view when close--%>
<%@attribute name="removable" type="java.lang.Boolean" %>
<%--show the refresh button or not--%>
<%@attribute name="hideRefresh" type="java.lang.Boolean" %>


<comm:peekIfEmpty var="modelName" value="${modelName}" />

<c:if test="${empty controllerName}">
    <c:set var="controllerName" value="view"/>
</c:if>

<c:if test="${empty removable}">
    <c:set var="removable" value="true"/>
</c:if>

<c:if test="${empty cssClass}">
    <c:set var="cssClass" value=""/>
</c:if>

<c:if test="${empty panelClass}">
    <c:set var="panelClass" value="panel-info"/>
</c:if>

<c:if test="${empty frameClass}">
    <c:set var="frameClass" value="col-xs-12 col-md-6 col-xl-4"/>
</c:if>

<div id="${id}" class="view display ${frameClass}" data-ng-init="modelName='${modelName}'; viewId='${id}';"
     data-ng-controller="${controllerName}Controller">

    <comm:push value="${id}" var="parentId" />
    <c:set var="classes" value="panel ${panelClass} ${cssClass}" />

    <div class="${classes}">
        <div class="panel-heading">
            <c:if test="${not empty icon}">
                <ctrl:icon value="${icon}"/> &nbsp;
            </c:if>
            <span class="title-text">${title}</span>

            <ctrl:icon value="remove" cssClass="cursor-pointer" alignRight="true">
                <jsp:attribute name="attributes">
                    <c:choose>
                        <c:when test="${removable}">
                            data-spg-close="${id}"
                        </c:when>
                        <c:otherwise>
                            data-spg-hide="${id}"
                        </c:otherwise>
                    </c:choose>
                </jsp:attribute>
            </ctrl:icon>

            <ctrl:icon value="chevron-{{hideBody ? 'down' : 'left'}}" cssClass="cursor-pointer" alignRight="true">
                <jsp:attribute name="attributes">data-spg-toggle="hideBody"</jsp:attribute>
            </ctrl:icon>

            <ctrl:icon value="arrow-up" cssClass="cursor-pointer" alignRight="true">
                <jsp:attribute name="attributes">data-spg-to-top</jsp:attribute>
            </ctrl:icon>

            <c:if test="${not hideRefresh}">
                <ctrl:icon value="repeat" cssClass="cursor-pointer" alignRight="true">
                    <jsp:attribute name="attributes"> data-spg-refresh='{"viewId":"${id}"}' </jsp:attribute>
                </ctrl:icon>
            </c:if>

            <jsp:invoke fragment="header"/>
        </div>
        <div id="${id}-body" class="panel-body" data-ng-hide="hideBody">
            <jsp:doBody/>
        </div>
    </div>

    <comm:pop var="parentId" />

</div>