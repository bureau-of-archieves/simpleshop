<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <d:contact-details path="shipName" />
        <d:contact-details path="orderDate" />
        <ctrl:detailsField path="requiredDate" />
    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>