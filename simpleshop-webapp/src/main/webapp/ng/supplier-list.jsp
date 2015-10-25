<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            {{item.contact.name}} {{item.contact.contactName | prefix:'(' | suffix: ')'}}
        </ctrl:summaryList>
    </t:view-list>

</t:page>