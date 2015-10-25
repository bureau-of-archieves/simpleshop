<%@include file="../WEB-INF/_header.jspf"%>


<t:page>


    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editModelLink path="supplier" />
        <ctrl:editModelLink path="categoryPrefix.prefix" label="Category" targetModelName="Category" prePostProcessor="propertyValue:prefix"/>
    </t:view-search>

</t:page>