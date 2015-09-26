<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <ctrl:detailsModelLink path="customer" />
        <ctrl:detailsModelLink path="employee" />
        <ctrl:detailsField path="orderDate" />
        <ctrl:detailsField path="requiredDate" />
        <ctrl:detailsField path="country" />

        <ctrl:detailsChildList path="orderItems" >
            <ctrl:detailsModelLink path="product" />
            <ctrl:detailsField path="quantity"/>
            <ctrl:detailsField path="sellPrice"/>
        </ctrl:detailsChildList>

        <fieldset>
            <legend><spring:message code="jsp.literal.deliveryDetails" /></legend>

            <ctrl:detailsModelLink path="shipper" />
            <ctrl:detailsField path="shippedDate" />
            <ctrl:detailsField path="numberOfParcels" />
            <ctrl:detailsField path="freight" />
            <ctrl:detailsField path="shipName" />
            <d:address-details path="shipAddress" />

        </fieldset>

    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>