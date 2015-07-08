<%@include file="_header.tag"%>
<%--update view base tag.--%>
<%@attribute name="modelId"%>
<%@attribute name="viewId"%>
<%@attribute name="title"%>
<%@attribute name="icon"%>


<t:view>
    <c:if test="${empty viewId}">
        <c:set var="viewId" value="${f:peek(stack, '_viewId')}"/>
    </c:if>

    <c:set var="modelName" value="${f:peek(stack, '_modelName')}" />

    <c:if test="${empty modelId}">
        <c:set var="modelId" value="${f:parseModelId(param.modelId, modelName) }" />
    </c:if>

    <c:if test="${empty title}">
        <c:set var="title" value="${f:peek(stack, '_friendlyModelName')} ${modelId} Details" />
    </c:if>

    <t:view-frame id="${viewId}" title="${title}" icon="${icon}" >
        <jsp:attribute name="header">
           <ctrl:icon value="pencil" cssClass="cursor-pointer" alignRight="true">
               <jsp:attribute name="attributes">
                   data-spg-update='{"modelName":"${modelName}","modelId":${modelId}}'
               </jsp:attribute>
           </ctrl:icon>
        </jsp:attribute>
        <jsp:body>
            <jsp:doBody/>
        </jsp:body>
    </t:view-frame>
</t:view>