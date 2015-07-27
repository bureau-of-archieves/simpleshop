<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editField path="contactNumber"/>
        <ctrl:editField path="address"/>
        <ctrl:editCombo path="suburb" />
        <ctrl:editField path="hireDateLower" />
        <ctrl:editField path="hireDateUpper" />
    </t:view-search>

    <script>
        <c:import url="/json/employee/search" />
    </script>
</t:page>