<%@include file="../_header.tag"%>


<t:view-search>

    <ctrl:editCombo path="customer" targetFriendlyModelName="Customer" descExpr="{0}.customerId + ' - ' + {0}.companyName"  />

</t:view-search>