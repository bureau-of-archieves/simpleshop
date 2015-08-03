<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <ctrl:detailsModelLink path="customer">
            {{targetObject.id}} - {{targetObject.contact.name}}
        </ctrl:detailsModelLink>
        <ctrl:detailsModelLink path="employee" >
            {{targetObject.id}} - {{targetObject.contact.name}}
        </ctrl:detailsModelLink>
        <ctrl:detailsField path="shipName" />
        <ctrl:detailsField path="orderDate" />
        <ctrl:detailsField path="requiredDate" />

        <ctrl:detailsChildList path="orderItems" >
            <ctrl:detailsModelLink path="product" />
            <ctrl:detailsField path="quantity"/>
            <ctrl:detailsField path="sellPrice"/>
        </ctrl:detailsChildList>

    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>