<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField path="customer.name" label="Customer Name" />
        <ctrl:editField path="customer.contactNumber" label="Contact Number" />
        <ctrl:editField path="employee.name" label="Employee Name" />
        <ctrl:editField path="employee.contactNumber" label="Contact Number" />
        <ctrl:editField path="shipName"/>
        <ctrl:editField path="orderDateLower" />
        <ctrl:editField path="orderDateUpper" />

    </t:view-search>

    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>