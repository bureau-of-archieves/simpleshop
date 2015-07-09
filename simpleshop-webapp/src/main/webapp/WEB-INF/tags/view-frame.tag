<%@include file="_header.tag" %>
<%--the id of this view, must be unique in the page--%>
<%@attribute name="id" required="true" %>
<%--title text of the frame panel--%>
<%@attribute name="title" required="true" %>
<%--the icon of the panel--%>
<%@attribute name="icon" %>
<%--the name of the ng controller of this view, which does not include the suffix 'Controller'.--%>
<%@attribute name="controller" %>
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
<%--just hide this view when close--%>
<%@attribute name="removable" type="java.lang.Boolean" %>
<%--show the refresh button or not--%>
<%@attribute name="hideRefresh" type="java.lang.Boolean" %>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>
<c:if test="${empty controller}">
    <c:set var="controller" value="view"/>
</c:if>
<c:if test="${not empty col}">
    <c:set var="col" value="${f:peek(stack, '_colPrefix')}${col}"/>
</c:if>
<c:if test="${empty removable}">
    <c:set var="removable" value="true"/>
</c:if>
<c:if test="${empty panelClass}">
    <c:set var="panelClass" value="panel-info"/>
</c:if>

<div id="${id}" class="view display ${col}" data-ng-init="modelName='${modelName}'; viewId='${id}';"
     data-ng-controller="${controller}Controller">

    ${f:_push(stack, "_parentId", id)}

    <div class="panel ${panelClass} ${cssClass}">
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
                    <jsp:attribute name="attributes"> data-spg-refresh="${id}" </jsp:attribute>
                </ctrl:icon>
            </c:if>

            <jsp:invoke fragment="header"/>
        </div>
        <div id="${id}-body" class="panel-body" data-ng-hide="hideBody">
            <jsp:doBody/>
        </div>
    </div>

    ${f:_pop(stack, "_parentId")}

</div>