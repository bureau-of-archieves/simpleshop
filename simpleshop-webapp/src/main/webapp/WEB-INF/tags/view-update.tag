<%@include file="_header.tag"%>
<%--update view base tag.--%>
<%@attribute name="modelId"%>
<%@attribute name="viewId"%>
<%@attribute name="title"%>
<%@attribute name="icon"%>


<t:view>

    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />

    <c:if test="${empty modelId}">
        <c:set var="modelId" value="${f:parseModelId(param.modelId, modelName) }" />
    </c:if>

    <c:if test="${empty viewId}">
        <c:set var="viewId" value="${f:peek(stack, '_viewId')}"/>
    </c:if>

    <c:if test="${empty title}">
        <c:set var="title" value="Update ${f:peek(stack, '_friendlyModelName')} ${modelId}" />
    </c:if>

    <t:view-frame id="${viewId}" title="${title}" icon="${icon}" >
        <jsp:attribute name="header">

        </jsp:attribute>
        <jsp:body>
            <ctrl:form name="${viewId}-form" editForm="true">

                <jsp:doBody/>

            </ctrl:form>

        </jsp:body>
    </t:view-frame>
</t:view>