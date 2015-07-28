<%@include file="../WEB-INF/_header.jspf"%>


<t:page>


    <t:view-search>
        <ctrl:editField path="name"/>
    </t:view-search>

    <script>
        <c:import url="/json/product/search" />
    </script>
</t:page>