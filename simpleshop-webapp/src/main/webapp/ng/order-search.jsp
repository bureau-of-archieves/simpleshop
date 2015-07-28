<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField path="shipName"/>

    </t:view-search>

    <script>
        <c:import url="/json/order/search" />
    </script>
</t:page>