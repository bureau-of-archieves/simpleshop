<%@include file="../_header.tag" %>
<%--encapsulates a bootstrap panel.--%>

<%--text that goes on titile bar--%>
<%@ attribute name="title" required="true" %>
<%--must be used, because used as id--%>
<%@ attribute name="name" required="false" %>
<%--currently must be built in bootstrap icon--%>
<%@ attribute name="icon" required="false" %>
<%--standard model type name, in camel case--%>
<%@ attribute name="modelName" required="false" %>
<%--optional ng controller name override, minus suffix "Controller"--%>
<%@ attribute name="controllerName" required="false" %>
<%--addition class to add to the panel div--%>
<%@attribute name="classes" required="false" %>
<%--whether to show edit icons--%>
<%@attribute name="editable" required="false" type="java.lang.Boolean" %>
<%--whether to wrap content in a form--%>
<%@attribute name="isForm" type="java.lang.Boolean" %>
<%@attribute name="header" fragment="true" %>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${_modelName}"/>
</c:if>

<c:if test="${empty name}">
    <c:set var="name" value="${modelName}-${f:deNull(_modelId, 'new') }"/>
</c:if>

<c:if test="${fn:contains(classes, ' display')}" >
    <c:set var="display" value="display" />
    <c:set var="classes" value="${fn:replace(classes, ' display', '')}" />
</c:if>

<c:if test="${classes == 'display'}">
    <c:set var="display" value="display" />
    <c:set var="classes" value="" />
</c:if>

<c:if test="${not empty col}" >
    <c:set var="col" value="col-sm-${col}" />
</c:if>


<div class="panel ${classes}" >
    ${f:emptyStr(f:push(stack, "_parentId", name))}
    <div class="panel-heading">
        <ctrl:icon value="" cssClass="cursor-pointer" alignRight="true">
            <jsp:attribute name="attributes"></jsp:attribute>
        </ctrl:icon>

            <span class="glyphicon glyphicon-remove pull-right cursor-pointer"
                    <c:if test="${editable}"> data-spg-close="${name}"  </c:if>
                    <c:if test="${not editable}"> data-spg-hide="${name}" </c:if>></span>

            <span class="glyphicon glyphicon-chevron-{{hideBody ? 'down' : 'left'}} pull-right cursor-pointer"
                  data-spg-toggle-body></span>

        <c:if test="${editable}">
            <r:modelAnchor modelName="${modelName}" idExpr="{{model.${modelName}Id}}" descExpr="{{model.name}}"/>
            <span class="glyphicon glyphicon-arrow-up pull-right cursor-pointer" data-spg-to-top></span>




        </c:if>
    </div>
    <div id="${name}Body" class="panel-body" data-ng-hide="hideBody">
        <c:if test="${isForm}">
            <div class="col-xs-11">
                <form name="${name}-form" class="form-horizontal" role="form" novalidate>
        </c:if>

        <jsp:doBody/>

        <c:if test="${isForm}">
                </form>
            </div>
        </c:if>
    </div>
    ${f:emptyStr(f:pop(stack, "_parentId"))}
</div>

