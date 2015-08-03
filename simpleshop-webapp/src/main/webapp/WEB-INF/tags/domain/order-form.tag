<%@include file="../_header.tag"%>

<ctrl:editCombo path="customer" showLink="true" />
<ctrl:editCombo path="employee" showLink="true" />
<ctrl:editField path="orderDate" />
<ctrl:editField path="requiredDate" />

<ctrl:editChildList path="orderItems" >
    <ctrl:editCombo path="product" />
    <ctrl:editField path="quantity" />
</ctrl:editChildList>

