<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            {{item.customer.contact.name}} On {{item.orderDate | date : 'yyyy-MM-dd' | na}}
        </ctrl:summaryList>
    </t:view-list>

</t:page>