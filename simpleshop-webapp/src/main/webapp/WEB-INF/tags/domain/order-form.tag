<%@include file="../_header.tag"%>

<ctrl:editModelLink path="customer" showLink="true" />
<ctrl:editModelLink path="employee" showLink="true" />
<ctrl:editField path="orderDate" />
<ctrl:editField path="requiredDate" />

<ctrl:editChildList path="orderItems" >
    <ctrl:editModelLink path="product" />
    <ctrl:editField path="quantity" />
</ctrl:editChildList>

