<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details>
        <ctrl:carousel path="images" hideEmpty="true" />
        <ctrl:detailsField path="name" />
        <ctrl:detailsField path="quantityPerUnit" />
        <ctrl:detailsInlineList path="categories">

            <span class="label label-primary" data-ng-init='makeCriteria(this, "Product", "category", item)'>
                <a href="javascript:void(0);" title="{{item.description}}"
                   data-spg-list="{&quot;modelName&quot;:&quot;Product&quot;, &quot;criteriaPath&quot;:&quot;criteria&quot;}">
                    {{item.name}}
                </a>
            </span>
        </ctrl:detailsInlineList>
        <ctrl:detailsField path="stock" />

        <ctrl:detailsChildList path="productSuppliers" >
            <ctrl:detailsModelLink path="supplier" />
            <ctrl:detailsField path="unitPrice"/>
            <ctrl:detailsField path="unitPriceDate"/>
            <ctrl:detailsField path="outOfStockDate"/>
            <ctrl:detailsField path="note"/>
        </ctrl:detailsChildList>

    </t:view-details>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/${param.modelId}" />
    </script>
</t:page>