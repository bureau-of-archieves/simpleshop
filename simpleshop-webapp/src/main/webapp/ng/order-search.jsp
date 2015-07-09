<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>

        <ctrl:editCombo path="customer" targetFriendlyModelName="Customer" descExpr="{0}.customerId + ' - ' + {0}.companyName"  />

    </t:view-search>

    <script>
        <c:import url="/json/order/search" />
    </script>
</t:page>