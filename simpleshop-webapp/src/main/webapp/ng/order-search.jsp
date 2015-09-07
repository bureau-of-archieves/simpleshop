<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField path="customer.name" />
        <ctrl:editField path="customer.contactNumber" />
        <ctrl:editField path="employee.name" />
        <ctrl:editField path="employee.contactNumber" />
        <ctrl:editField path="shipName"/>
        <ctrl:editField path="orderDateLower" />
        <ctrl:editField path="orderDateUpper" />

    </t:view-search>

    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>