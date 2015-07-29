<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <d:contact-details path="contact" />
    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>