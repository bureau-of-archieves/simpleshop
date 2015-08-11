<%@include file="../_header.tag"%>

<%@attribute name="path" required="true" %>

<c:set var="base" value="${f:peek(stack, '_base')}${path}." />
<c:set var="modelName" value="Contact" />

${f:_push(stack, '_base', base)}
${f:_push(stack, '_modelName', modelName)}

<ctrl:detailsField path="name" />
<ctrl:detailsField path="contactName" displayFormat="na" />
<d:address-details path="address" />

<ctrl:mapDetails path="contactNumbers" />

${f:_pop(stack, '_base')}
${f:_pop(stack, '_modelName')}