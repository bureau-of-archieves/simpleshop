<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField    path="companyName"/>
        <ctrl:editField    path="contactName"/>
        <ctrl:editField    path="city"/>
    </t:view-search>

    <script>
        <c:import url="/json/supplier/search" />
    </script>
</t:page>