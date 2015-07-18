<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-update>
        <d:customer-form />
    </t:view-update>

    <script>
        <c:import url="/json/customer/${param.modelId}" />
    </script>
</t:page>