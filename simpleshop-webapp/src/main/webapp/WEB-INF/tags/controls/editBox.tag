<%@include file="../_header.tag" %>

<%@attribute name="parentId" required="false" %>


<c:if test="${empty parentId}" >
    <c:set var="parentId" value="${f:peek(stack, '_parentId')}" />
</c:if>

<div data-ng-show="model" class="row panel-content">
    <div class="col-xs-10">

        <form name="${parentId}-form" class="form-horizontal" role="form">

            <jsp:doBody/>

            <br>

            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <button type="submit" class="btn btn-primary" data-cw_save="${parentId}">Save</button>
                    <button type="submit" class="btn btn-primary" data-spg-cancel="${parentId}">Cancel</button>
                </div>
            </div>
        </form>
    </div>
</div>