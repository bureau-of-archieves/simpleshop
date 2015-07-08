<%@include file="_header.tag"%>
<%--
    The root tag that all jsp pages should use to wrap all content
    All global config are set here.
--%>


${f:_push(stack, "_base", "model.")}
${f:_push(stack, "_colPrefix", "col-sm-")}


<jsp:doBody/>


${f:_pop(stack, "_base")}
${f:_pop(stack, "_colPrefix")}

