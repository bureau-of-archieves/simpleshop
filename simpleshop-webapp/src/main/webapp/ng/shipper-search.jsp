<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <ctrl:editField path="companyName"/>
    <ctrl:editField path="phone"/>

    <script>
        <c:import url="/json/shipper/search" />
    </script>
</t:page>