<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <ctrl:modelLink path="customer">
            {{targetObject.id}} - {{targetObject.contact.name}}
        </ctrl:modelLink>
        <ctrl:modelLink path="employee" >
            {{targetObject.id}} - {{targetObject.contact.name}}
        </ctrl:modelLink>
        <ctrl:detailsField path="shipName" />
        <ctrl:detailsField path="orderDate" />
        <ctrl:detailsField path="requiredDate" />
    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>