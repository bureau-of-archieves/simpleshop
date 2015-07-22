<%@include file="../_header.tag"%>
<%@attribute name="path" required="true" %>

<c:set var="base" value="${f:peek(stack, '_base')}${path}." />
${f:_push(stack, '_base', base)}

<div class="col-sm-6 details-field">

    <div class="row">
        <div class="col-sm-5"><label> Address</label></div>
        <div class="col-sm-7 hide-children"  >
            <address class="display" data-ng-show="${base}addressLine1 || ${base}addressLine2 || ${base}suburb">
                <span>{{${base}addressLine1}}</span>
                <span>{{${base}addressLine2 | prefix:', '}}</span>
                <d:suburb-details path="suburb" />
            </address>
            <span class="display no-display-predecessor">N/A</span>
        </div>
    </div>
</div>

${f:_pop(stack, "_base")}
