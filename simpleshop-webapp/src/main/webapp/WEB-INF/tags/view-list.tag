<%@include file="_header.tag"%>

<c:set var="viewId" value="&${f:uuid()};"/>
<t:view>
    ${f:_push(stack, "_viewId", viewId)}
    ${f:_push(stack, "_replace_id_marker", viewId)}

    <t:view-frame id="${viewId}" title="${f:friendlyModelNameFromUrl(pageContext.request.requestURL)}" >
        <jsp:attribute name="header">

        </jsp:attribute>
        <jsp:body>
            <jsp:doBody/>
        </jsp:body>
    </t:view-frame>

    ${f:_pop(stack, "_viewId")}
</t:view>
