<%@include file="../WEB-INF/_header.jspf" %>

<t:page>
    <t:view-list>

        <div class="row" data-ng-repeat="item in model" data-ng-init='makeCriteria(this, "Product", "category", item)'>
            <div class="col-xs-12" >
                <a href="javascript:void(0);" class="thumbnail" data-spg-list="{&quot;modelName&quot;:&quot;Product&quot;, &quot;criteriaPath&quot;:&quot;criteria&quot;}">
                    <img data-ng-src="${pageContext.request.contextPath}assets/img/{{item.imagePath}}" alt="{{item.description}}">
                </a>
            </div>
            <div class="caption" style="position:relative;top:-5.5em; right:2em; float:right;" >
               <h3><span>{{item.name}}</span></h3>
            </div>
        </div>

    </t:view-list>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>