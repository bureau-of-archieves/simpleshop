<%@include file="../_header.tag"%>

<ctrl:editModelLink path="customer" showLink="true" />
<ctrl:editModelLink path="employee" showLink="true" />
<ctrl:editField path="orderDate" />
<ctrl:editField path="requiredDate" />
<d:selectCountry path="country" />

<ctrl:editChildList path="orderItems" >
    <ctrl:editModelLink path="product" />
    <ctrl:editField path="quantity" />
    <ctrl:editField path="sellPrice" />
    <ctrl:editModelLink path="supplier" />
</ctrl:editChildList>

<spring:message var="literal_deliveryDetails" code="jsp.literal.deliveryDetails" />
<comm:separatorLine title="${literal_deliveryDetails}" />

<ctrl:editModelLink path="shipper" showLink="true" />
<ctrl:editField path="shippedDate" />
<ctrl:editField path="numberOfParcels" />
<ctrl:editField path="freight" />
<ctrl:editField path="shipName" />
<d:address-form path="shipAddress" />

