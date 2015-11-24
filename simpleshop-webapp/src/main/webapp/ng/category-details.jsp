<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <t:view-details>
        <ctrl:detailsImg path="imagePath" />
        <ctrl:detailsField path="name" />
        <ctrl:detailsField path="description" />
        <ctrl:detailsField path="menuItem" />
        <ctrl:detailsModelLink path="parent" />

    </t:view-details>
</t:page>