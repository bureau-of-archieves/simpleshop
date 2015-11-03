<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-details frameClass="col-xs-12">
        <comm:push var="fieldCss" value="col-lg-6" />
        <comm:push var="labelCss" value="col-sm-4 col-xl-3" />
        <comm:push var="detailsCss" value="col-sm-8 col-xl-9" />

        <div class="row">
            <div class="col-lg-6 clearfix">
                <ctrl:carousel path="images"  />
            </div>
            <div class="col-lg-6">
                <ctrl:detailsField path="name" />
                <ctrl:detailsField path="quantityPerUnit" />
                <ctrl:detailsInlineList path="categories">

            <span class="label label-primary" data-ng-init='makeCriteria(this, "Product", "category", item)'>
                <a href="javascript:void(0);" title="{{item.description}}"
                   data-spg-list='{"modelName":"Product", "criteriaPath":"criteria","variant":"main"}'>
                    {{item.name}}
                </a>
            </span>
                </ctrl:detailsInlineList>
                <ctrl:detailsField path="stock" />
            </div>
        </div>
        <comm:pop var="fieldCss" />
        <comm:pop var="labelCss" />
        <comm:pop var="detailsCss" />

        <div class="col-sm-12">
            <comm:peek var="base" />
            <button type="button" class="btn btn-warning" data-spg-cart-add="{{${base}id}}" ><spring:message code="jsp.literal.addToCart" /></button>
        </div>

    </t:view-details>

</t:page>