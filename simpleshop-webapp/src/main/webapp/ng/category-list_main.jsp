<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-list frameClass="col-xs-12">

        <comm:peek var="imgBase" />
        <c:set var="actionDirective" value=' ' />
        <div class="row">
            <div class="col-xs-12 col-md-6 col-lg-3 col-xl-2" data-ng-repeat="item in model"
                 data-ng-init='makeCriteria(this, "Product", "category", item)'>
                <a href="javascript:void(0);" class="thumbnail"
                   data-spg-list="{&quot;modelName&quot;:&quot;Product&quot;, &quot;criteriaPath&quot;:&quot;criteria&quot;, &quot;variant&quot;:&quot;main&quot;}">
                    <img style="min-width: 100%; height: auto;" data-ng-src="${imgBase}{{item.imagePath}}"
                         alt="{{item.description}}">
                </a>

                <div class="caption" style="position:relative;top:-5.5em; right:2em; float:right;">
                    <a href="javascript:void(0)"
                       data-spg-list="{&quot;modelName&quot;:&quot;Product&quot;, &quot;criteriaPath&quot;:&quot;criteria&quot;, &quot;variant&quot;:&quot;main&quot;}">
                        <h3><span>{{item.name}}</span></h3>
                    </a>
                </div>
            </div>

        </div>

    </t:view-list>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>