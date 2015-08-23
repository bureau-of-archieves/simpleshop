<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <ctrl:carousel path="images" hideEmpty="true" />
        <ctrl:detailsField path="name" />
        <ctrl:detailsField path="quantityPerUnit" />
        <ctrl:detailsField path="stock" />
    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>