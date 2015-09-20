<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField path="keywords"/>
        <ctrl:editNgSelect path="country" optionsUrl="/json/countries" optionsExpression="country.name for country in items"  />
    </t:view-search>

    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>