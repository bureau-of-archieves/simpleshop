<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <t:view-search>
        <ctrl:editField path="name"/>
        <ctrl:editField path="contactNumber"/>
        <ctrl:editField path="address"/>
        <ctrl:editModelLink path="suburb" />
    </t:view-search>
</t:page>