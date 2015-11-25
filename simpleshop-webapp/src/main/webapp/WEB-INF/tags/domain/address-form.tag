<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>

<%@attribute name="path" required="true" %>

<comm:peek var="base" />
<comm:push value="${base}${path}." var="base" />
<comm:push value="Address" var="modelName" />

<ctrl:editField path="addressLine1" />
<ctrl:editField path="addressLine2" />
<ctrl:editModelLink path="suburb" />

<comm:pop var="base" />
<comm:pop var="modelName" />