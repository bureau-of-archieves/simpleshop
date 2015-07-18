<%@include file="_header.tag"%>
<%--update view base tag.--%>
<%@attribute name="modelId"%>
<%@attribute name="title"%>
<%@attribute name="icon"%>

<t:view>

    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />

    <c:if test="${empty modelId}">
        <c:set var="modelId" value="${f:parseModelId(param.modelId, modelName) }" />
    </c:if>

    <c:set var="viewType" value="${f:peek(stack, '_viewType')}" />
    <c:set var="viewId" value="${f:pascalNameToUrlName(modelName)}-${viewType}-${modelId}"/>

    <c:if test="${empty title}">
        <c:set var="title" value="Update ${f:peek(stack, '_friendlyModelName')} ${modelId}" />
    </c:if>

    ${f:_push(stack, "_viewId", viewId)}

    <t:view-frame id="${viewId}" title="${title}" icon="${icon}" >
        <jsp:attribute name="header">

        </jsp:attribute>
        <jsp:body>
            <ctrl:form name="${viewId}-form" editForm="true">

                <jsp:doBody/>

            </ctrl:form>

        </jsp:body>
    </t:view-frame>

    ${f:_pop(stack, "_viewId")}
</t:view>