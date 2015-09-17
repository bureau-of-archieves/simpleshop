<%@include file="../WEB-INF/_header.jspf" %>


<t:page>
    <t:view-list>
        <ctrl:summaryList modelId="{{item.id | json}}">
            <div style="display: inline-block; margin-top: 5px; vertical-align: middle;">
                {{item.name}} ({{item.quantityPerUnit}} per unit)

                <span class="label label-primary" data-ng-repeat="cat in item.categories" style="margin-left: 0.5em;">{{cat.name}}</span>
            </div>
        </ctrl:summaryList>
    </t:view-list>
    <script>
        <c:import url="/json/${f:urlModelNameFromUrl(pageContext.request.requestURL)}/search" />
    </script>
</t:page>