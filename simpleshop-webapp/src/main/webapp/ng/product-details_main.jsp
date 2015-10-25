<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details frameClass="col-xs-12">
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

        <div class="col-sm-12">
            <comm:peek var="base" />
            <button type="button" class="btn btn-primary" data-spg-cart-add="{{${base}id}}" ><spring:message code="jsp.literal.addToCart" /></button>
        </div>

    </t:view-details>

</t:page>