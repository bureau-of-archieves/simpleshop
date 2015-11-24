<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editModelLink path="prefix" label="Ancestor" targetModelName="Category" prePostProcessor="propertyValue:prefix"/>
        <ctrl:editModelLink path="parentCategory" />

    </t:view-search>

</t:page>