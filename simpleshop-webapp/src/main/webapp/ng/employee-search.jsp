<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField    path="firstName"/>
        <ctrl:editField    path="lastName"/>
        <ctrl:editField    path="title"/>
        <ctrl:editField    path="birthDateLower"/>
        <ctrl:editField    path="birthDateUpper"/>
    </t:view-search>

    <script>
        <c:import url="/json/employee/search" />
    </script>
</t:page>