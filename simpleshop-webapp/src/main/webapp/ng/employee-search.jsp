<%@include file="../WEB-INF/_header.jspf"%>


<t:page>
    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editField path="contactNumber"/>
        <ctrl:editField path="address"/>
        <ctrl:editModelLink path="suburb" />
        <ctrl:editField path="hireDateLower" />
        <ctrl:editField path="hireDateUpper" />
    </t:view-search>

</t:page>