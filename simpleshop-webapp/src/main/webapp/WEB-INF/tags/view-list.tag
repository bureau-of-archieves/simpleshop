<%-- All create views are wrapped in this tag..--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="sponge/functions" %>

<%--########################## ATTRIBUTES ################################--%>
<%--bootstrap view frame css class--%>
<%@attribute name="frameClass" %>

<%--########################## TAG CONTENT ################################--%>
<t:view>
    <comm:push value="&${f:uuid()};" var="viewId" />
    <comm:push value="${viewId}" var="replace_id_marker" />

    <t:view-frame id="${viewId}" title="${f:friendlyModelNameFromUrl(pageContext.request.requestURL)}" frameClass="${frameClass}" cssClass="list-view">
        <jsp:attribute name="header">
        </jsp:attribute>

        <jsp:body>
            <jsp:doBody/>
        </jsp:body>
    </t:view-frame>

    <comm:pop var="viewId" />
</t:view>
