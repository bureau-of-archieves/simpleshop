<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-list frameClass="col-xs-12">

        <comm:peek var="imgBase" />
        <div class="row">
            <div class="tile-main col-xs-12 col-md-6 col-lg-3 col-xl-2" data-ng-repeat="item in model">
                <a href="javascript:void(0);" class="thumbnail"
                   data-spg-details="{&quot;modelName&quot;:&quot;Product&quot;, &quot;modelId&quot;:&quot;{{item.id}}&quot;, &quot;variant&quot;:&quot;main&quot;}">
                    <img style="min-width: 100%; height: auto;" data-ng-src="${imgBase}{{item.images | defaultImage}}"
                         alt="Image of {{item.name}}">
                </a>

                <div class="caption">
                    <a href="javascript:void(0)"
                       data-spg-details="{&quot;modelName&quot;:&quot;Product&quot;, &quot;modelId&quot;:&quot;{{item.id}}&quot;, &quot;variant&quot;:&quot;main&quot;}">
                        <h3><span>{{item.name}}</span> - <span>{{item.quantityPerUnit}}</span></h3>
                    </a>
                </div>
            </div>

            <div class="well well-md" data-ng-show="model.length == 0"><spring:message code="jsp.literal.noProductFound" /></div>

        </div>

    </t:view-list>

</t:page>