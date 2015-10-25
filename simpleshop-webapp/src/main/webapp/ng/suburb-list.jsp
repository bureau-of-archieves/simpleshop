<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            {{item.suburb}} {{item.postcode | prefix:', '}} {{item.country.name | prefix:', '}}
        </ctrl:summaryList>
    </t:view-list>

</t:page>