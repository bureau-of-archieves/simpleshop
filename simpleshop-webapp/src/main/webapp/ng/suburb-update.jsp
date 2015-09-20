<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-update>
        <d:suburb-form />
    </t:view-update>

    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>