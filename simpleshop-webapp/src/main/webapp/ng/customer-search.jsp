<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editField path="contactNumber"/>
        <ctrl:editField path="address"/>
        <ctrl:editCombo path="suburb" />
    </t:view-search>

    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>