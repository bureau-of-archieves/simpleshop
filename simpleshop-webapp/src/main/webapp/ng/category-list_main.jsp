<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:page>
    <t:view-list title="All Categories" frameClass="col-xs-12">

        <comm:peek var="imgBase" />
        <c:set var="actionDirective" value=' ' />
        <div class="row">
            <div class="tile-main col-xs-12 col-md-6 col-lg-3 col-xl-2" data-ng-repeat="item in model"
                 data-ng-init='makeCriteria(this, "Product", "category", item)'>
                <a href="javascript:void(0);" class="thumbnail"
                   data-spg-list="{&quot;modelName&quot;:&quot;Product&quot;, &quot;criteriaPath&quot;:&quot;criteria&quot;, &quot;variant&quot;:&quot;main&quot;}">
                    <img style="min-width: 100%; height: auto;" data-ng-src="${imgBase}{{item.imagePath}}"
                         alt="{{item.description}}">
                </a>

                <div class="caption" >
                    <a href="javascript:void(0)"
                       data-spg-list="{&quot;modelName&quot;:&quot;Product&quot;, &quot;criteriaPath&quot;:&quot;criteria&quot;, &quot;variant&quot;:&quot;main&quot;}">
                        <h3><span>{{item.name}}</span></h3>
                    </a>
                </div>
            </div>

        </div>

    </t:view-list>

</t:page>