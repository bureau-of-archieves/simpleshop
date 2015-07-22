<%@include file="../_header.tag"%>
<%@attribute name="path" required="true" %>

<c:set var="base" value="${f:peek(stack, '_base')}${path}." />
${f:_push(stack, '_base', base)}
<span>{{${base}suburb}} {{${base}state}} {{${base}postcode}}</span>
${f:_pop(stack, "_base")}