<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            {{item.name}}
        </ctrl:summaryList>
    </t:view-list>
    <script>
        <c:import url="/json/product/search" />
    </script>
</t:page>