<%-- All details views are wrapped in this tag.--%>
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
    <c:set var="modelId" value="${f:parseModelId(param.modelId, modelName) }" />
    <c:if test="${empty title}">
        <comm:peek var="friendlyModelName" />
        <spring:message var="literal_details" code="jsp.literal.details" />
        <c:set var="title" value="${friendlyModelName} ${modelId} ${literal_details}" />
    </c:if>

    <%--########################## TAG CONTENT ################################--%>
    <comm:peek var="viewType" />
    <c:set var="viewId" value="${f:pascalNameToUrlName(modelName)}-${viewType}-${modelId}"/>

    <t:view-frame id="${viewId}" title="${title}" icon="${icon}" cssClass="details-view" >
        <jsp:attribute name="header">
           <ctrl:icon value="pencil" cssClass="cursor-pointer" alignRight="true">
               <jsp:attribute name="attributes">
                   data-spg-update='{"modelName":"${modelName}","modelId":${modelId}}'
               </jsp:attribute>
           </ctrl:icon>
        </jsp:attribute>
        <jsp:body>
            <jsp:doBody/>
        </jsp:body>
    </t:view-frame>
</t:view>