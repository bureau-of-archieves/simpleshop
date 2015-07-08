<%@include file="../_header.tag" %>
<%@attribute name="classes" %>
<div data-ng-show="model" class="row panel-content">
    <div class="col-xs-12  ${classes}">

            <jsp:doBody/>

    </div>
</div>