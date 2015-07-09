<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <ctrl:editField    path="idList"/>
    <ctrl:editField    path="namePattern"/>
    <ctrl:editCheckbox path="inStock"/>

    <script>
        <c:import url="/json/product/search" />
    </script>
</t:page>