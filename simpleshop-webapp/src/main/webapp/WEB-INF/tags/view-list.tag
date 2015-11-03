<%-- All create views are wrapped in this tag..--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="sponge/functions" %>

<%--########################## ATTRIBUTES ################################--%>
<%--bootstrap view frame css class--%>
<%@attribute name="frameClass" %>
<%@attribute name="title" %>

<%--########################## TAG CONTENT ################################--%>
<c:if test="${empty title}" >
    <c:set var="title" value="${f:friendlyModelNameFromUrl(pageContext.request.requestURL)} List" />
</c:if>
<t:view>
    <comm:push value="&${f:uuid()};" var="viewId" />
    <comm:push value="${viewId}" var="replace_id_marker" />

    <t:view-frame id="${viewId}" title="${title}" frameClass="${frameClass}" cssClass="list-view">
        <jsp:attribute name="header">
        </jsp:attribute>

        <jsp:body>
            <jsp:doBody/>
        </jsp:body>
    </t:view-frame>

    <comm:pop var="viewId" />
</t:view>
