<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            {{item.contact.name}} {{item.contact.contactName | prefix:'(' | suffix: ')'}}
        </ctrl:summaryList>
    </t:view-list>
</t:page>