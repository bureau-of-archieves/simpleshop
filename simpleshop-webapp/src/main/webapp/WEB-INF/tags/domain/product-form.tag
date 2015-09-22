<%@include file="../_header.tag"%>

<ctrl:editField path="name" />
<ctrl:editField path="quantityPerUnit" />
<ctrl:editInlineList path="categories">
    <span class="label label-primary">{{item.name}} <span class="glyphicon glyphicon-remove" data-ng-click="remove(item)"></span></span>
</ctrl:editInlineList>
<ctrl:editField path="stock" />

<hr>

<ctrl:editChildList path="productSuppliers" >
    <ctrl:editModelLink path="supplier" />
    <ctrl:editField path="unitPrice" />
    <ctrl:editField path="url" />
    <ctrl:editField path="outOfStockDate" />
    <ctrl:editField path="note" />
</ctrl:editChildList>
