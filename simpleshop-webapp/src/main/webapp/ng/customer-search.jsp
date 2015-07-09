<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editField path="address"/>
    </t:view-search>

    <script>
        <c:import url="/json/customer/search" />
    </script>
</t:page>