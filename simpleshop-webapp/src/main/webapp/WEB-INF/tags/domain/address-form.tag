<%@include file="../_header.tag"%>
<%@attribute name="path" required="true" %>

<c:set var="base" value="${f:peek(stack, '_base')}${path}." />
<c:set var="modelName" value="Address" />

${f:_push(stack, '_base', base)}
${f:_push(stack, '_modelName', modelName)}

<ctrl:editField path="addressLine1" />
<ctrl:editField path="addressLine2" />
<ctrl:editCombo path="suburb" />

${f:_pop(stack, '_base')}
${f:_pop(stack, '_modelName')}