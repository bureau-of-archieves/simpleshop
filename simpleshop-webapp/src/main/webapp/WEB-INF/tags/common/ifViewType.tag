<%-- This control renders its body only when in the specified view type. --%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="viewType" required="true" %>


<%--########################## TAG CONTENT ################################--%>
<c:set var="visibleViewType" value="${viewType}" />
<comm:peek var="viewType" />
<c:if test="${fn:startsWith(viewType, visibleViewType.concat('_') ) || viewType == visibleViewType}" >
    <jsp:doBody />
</c:if>