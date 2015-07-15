<%@include file="_header.tag"%>
<%--create view base tag.--%>

<%@attribute name="title"%>
<%@attribute name="icon"%>

<c:set var="viewId" value="&${f:uuid()};"/>
<t:view>
    ${f:_push(stack, "_viewId", viewId)}
    ${f:_push(stack, "_replace_id_marker", viewId)}

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

    ${f:_pop(stack, "_viewId")}
</t:view>