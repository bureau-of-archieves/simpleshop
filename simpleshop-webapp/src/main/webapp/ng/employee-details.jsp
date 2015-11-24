<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="d" tagdir="/WEB-INF/tags/domain" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>

<t:page>
    <t:view-details>
        <d:contact-details path="contact" />
        <ctrl:detailsField path="hireDate" displayFormat="| na" />
    </t:view-details>
</t:page>

