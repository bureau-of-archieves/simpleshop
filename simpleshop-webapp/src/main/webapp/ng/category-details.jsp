<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <ctrl:detailsImg path="imagePath" />
        <ctrl:detailsField path="name" />
        <ctrl:detailsField path="description" />
        <ctrl:detailsField path="menuItem" />
        <ctrl:detailsModelLink path="parent" />

    </t:view-details>

</t:page>