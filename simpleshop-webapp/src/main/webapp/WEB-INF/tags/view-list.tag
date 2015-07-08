<%@include file="_header.tag"%>
<%--list view base tag.--%>
<%@attribute name="viewId" %>

<t:view>
    <c:if test="${empty viewId}">
        <c:set var="viewId" value="${f:peek(stack, '_viewId')}"/>
    </c:if>

    <t:view-frame id="${viewId}" title="${f:friendlyModelNameFromUrl(viewId)}" >
        <jsp:attribute name="header">

        </jsp:attribute>
        <jsp:body>
            <jsp:doBody/>
        </jsp:body>
    </t:view-frame>
</t:view>