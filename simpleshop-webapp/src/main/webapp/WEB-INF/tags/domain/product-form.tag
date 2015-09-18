<%@include file="../_header.tag"%>

<ctrl:editField path="name" />
<ctrl:editField path="quantityPerUnit" />
<ctrl:editInlineList path="categories">
    <span class="label label-primary">{{item.name}} <span class="glyphicon glyphicon-remove" data-ng-click="remove(item)"></span></span>
</ctrl:editInlineList>
<ctrl:editField path="stock" />
