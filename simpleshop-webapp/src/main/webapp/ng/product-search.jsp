<%@include file="../WEB-INF/_header.jspf"%>


<t:page>


    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editModelLink path="supplier" />
    </t:view-search>

    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>