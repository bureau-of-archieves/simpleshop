<%@include file="../_header.tag" %>

<%--json string--%>
<%@attribute name="modelId" required="true" %>
<%@attribute name="modelName" required="false" %>

<c:if test="${empty modelName}">
    <c:set var="modelName" value="${f:peek(stack, '_modelName')}"/>
</c:if>

<c:set var="friendlyMmodelName" value="${f:peek(stack, '_friendlyMmodelName')}"/>

<h4>Search result for ${friendlyMmodelName}</h4>

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
<button type="submit" class="btn btn-primary" data-spg-create="${modelName}">Create New</button>




