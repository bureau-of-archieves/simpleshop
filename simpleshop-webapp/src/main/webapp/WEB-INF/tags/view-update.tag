<%-- All create views are wrapped in this tag..--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="title"%>
<%@attribute name="icon"%>

<t:view>

    <%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
    <comm:peek var="modelName" />
    <comm:push value="${f:parseModelId(param.modelId, modelName) }" var="modelId" />
    <c:if test="${empty title}">
        <spring:message var="literal_update" code="jsp.literal.update" />
        <comm:peek var="friendlyModelName" />
        <c:set var="title" value="${literal_update} ${friendlyModelName} ${modelId}" />
    </c:if>

    <%--########################## TAG CONTENT ################################--%>
    <comm:peek var="viewType" />
    <comm:push value="${f:pascalToUrl(modelName)}-${viewType}-${modelId}" var="viewId" />

    <t:view-frame id="${viewId}" title="${title}" icon="${icon}" cssClass="update-view">
        <jsp:attribute name="header">
        </jsp:attribute>

        <jsp:body>
            <ctrl:form name="${viewId}-form" editForm="true">

                <jsp:doBody/>

            </ctrl:form>
        </jsp:body>
    </t:view-frame>

    <comm:pop var="viewId" />
    <comm:pop var="modelId" />
</t:view>