<%@include file="../_header.tag" %>

<%--json string--%>
<%@attribute name="modelId" required="true" %>
<%@attribute name="modelName" required="false" %>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:set var="friendlyMmodelName" value="${f:peek(stack, '_friendlyMmodelName')}"/>

<div class="row">
    <div class="${f:peek(stack, "_colPrefix")}8">
        <h4>Search result for ${friendlyMmodelName}</h4>
    </div>
    <div class="${f:peek(stack, "_colPrefix")}4 clearfix">
        <form class="form-inline pull-right">
            <div class="input-group">
                <span class="input-group-addon" id="sizing-addon2">Sort By</span>
                <select class="selectpicker form-control">
                    <option>Field 1 Asc</option>
                    <option>Field 2 Desc</option>
                    <option>Field 3 Asc</option>
                </select>
            </div>
        </form>
    </div>
</div>


<div class="list-group">

    <a href="javascript:void(0);" data-spg-details='{"modelName":"${modelName}","modelId":${modelId}}'
       class="list-group-item clearfix" data-ng-repeat="item in model">
        <jsp:doBody/>

        <button class="btn btn-sm btn-danger pull-right" data-ng-mouseover="$event.stopPropagation();" data-spg-delete='{"modelName":"${modelName}","modelId":${modelId}}'>
            Delete
        </button>
    </a>


    <a class="list-group-item" data-ng-show="model.length == 0">No result found.</a>

</div>
<div class="row">

    <div class="${f:peek(stack, "_colPrefix")}9">
        <button type="submit" class="btn btn-primary" data-spg-create="${modelName}">Create New</button>
    </div>
    <div class="${f:peek(stack, "_colPrefix")}3">
        <nav>
            <ul class="pager">
                <li class="previous {{previousEnabled()}}" ><a href="#" data-spg-refresh='{"pageDelta":-1}'><span aria-hidden="true">&larr;</span> Older</a></li>
                <li class="next {{nextEnabled()}}" ><a href="#" data-spg-refresh='{"pageDelta":1}'>Newer <span aria-hidden="true">&rarr;</span></a></li>
            </ul>
        </nav>
    </div>
</div>






