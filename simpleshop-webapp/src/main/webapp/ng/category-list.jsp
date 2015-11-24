<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            <ctrl:indent source="item.prefix" pattern="_" delta="-1" />
            <span title="{{item.description}}">{{item.id}} - {{item.name}}</span>
        </ctrl:summaryList>
    </t:view-list>

</t:page>