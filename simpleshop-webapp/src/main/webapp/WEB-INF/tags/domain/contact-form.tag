<%@include file="../_header.tag"%>
<%@attribute name="path" required="true" %>

<c:set var="base" value="${f:peek(stack, '_base')}${path}." />
<%--override model type of the container--%>
<c:set var="modelName" value="Contact" />

${f:_push(stack, '_base', base)}
${f:_push(stack, '_modelName', modelName)}

<ctrl:editField path="name" />
<ctrl:editField path="contactName" />
<d:address-form path="address" />
<ctrl:editMap path="contactNumbers" />
<ctrl:editField path="note" />

${f:_pop(stack, '_base')}
${f:_pop(stack, '_modelName')}