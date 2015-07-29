<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <d:contact-details path="contact" />
        <ctrl:detailsField path="hireDate" displayFormat="na" />
    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>

