<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <d:contact-details path="contact" />
        <ctrl:detailsField path="stock" displayFormat="yesno" />
    </t:view-details>
    <script>
        <c:import url="/json/customer/${param.modelId}" />
    </script>
</t:page>