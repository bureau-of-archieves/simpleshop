<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <ctrl:detailsImg path="imagePath" />
        <ctrl:detailsField path="name" />
        <ctrl:detailsField path="description" />
        <ctrl:detailsField path="menuItem" displayFormat="yesno"/>
        <ctrl:detailsField path="parent" />

    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>