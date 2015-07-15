<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.customerId | json}}">
            {{item.contact.name}}
        </ctrl:summaryList>
    </t:view-list>
    <script>
        <c:import url="/json/customer/search" />
    </script>
</t:page>