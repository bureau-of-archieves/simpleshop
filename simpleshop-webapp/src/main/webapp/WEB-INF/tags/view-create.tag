<%@include file="_header.tag"%>
<%--create view base tag.--%>

<%@attribute name="viewId"%>
<%@attribute name="title"%>
<%@attribute name="icon"%>

<t:view>
    <c:if test="${empty viewId}">
        <c:set var="viewId" value="${f:peek(stack, '_viewId')}"/>
    </c:if>
    <c:if test="${empty title}">
        <c:set var="title" value="Create New ${f:peek(stack, '_friendlyModelName')}" />
    </c:if>

    <t:view-frame id="${viewId}" title="${title}" >
        <jsp:attribute name="header">

        </jsp:attribute>
        <jsp:body>
            <ctrl:form name="${viewId}-form" editForm="true">

                <jsp:doBody/>

            </ctrl:form>
        </jsp:body>


    </t:view-frame>
</t:view>