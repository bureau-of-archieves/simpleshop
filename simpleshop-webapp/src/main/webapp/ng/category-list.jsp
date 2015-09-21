<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            <ctrl:indent source="item.prefix" pattern="_" delta="-1" />
            <span title="{{item.description}}">{{item.id}} - {{item.name}}</span>
        </ctrl:summaryList>
    </t:view-list>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>