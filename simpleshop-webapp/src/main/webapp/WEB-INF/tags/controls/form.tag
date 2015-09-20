<%@include file="../_header.tag" %>
<%@attribute name="name" required="true" %>
<%@attribute name="editForm" type="java.lang.Boolean" %>

<c:set var="viewId" value="${f:peek(stack, '_viewId')}" />

<div class="col-xs-11">
    <form name="${name}" class="form-horizontal" role="form" novalidate>

        <jsp:doBody/>

        <c:if test="${editForm}">
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <button type="submit" class="btn btn-primary" data-ng-disabled="isUnchanged()" data-spg-save="${viewId}">Save</button>
                    <button type="submit" class="btn btn-primary" data-spg-cancel="${viewId}">Cancel</button>
                    <button type="submit" class="btn btn-primary" data-ng-disabled="isUnchanged()" data-ng-click="reset()">Reset</button>
                </div>
            </div>
        </c:if>

    </form>
</div>
