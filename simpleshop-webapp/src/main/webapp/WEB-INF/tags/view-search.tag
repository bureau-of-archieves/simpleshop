<%-- All create views are wrapped in this tag..--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="d" tagdir="/WEB-INF/tags/domain" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="sponge/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="stack" class="simpleshop.webapp.util.ViewValueStackBean" scope="request" />

<%--########################## TAG CONTENT ################################--%>
<t:view>
    <comm:peek var="modelName" />
    <comm:push value="${modelName}" var="targetModelName" />
    <comm:peek var="friendlyModelName" />
    <comm:push value="${friendlyModelName}" var="targetFriendlyModelName" />
    <comm:peek var="viewType" />

    <comm:push value="${f:pascalToUrl(targetModelName)}-${viewType}" var="viewId" />
    <comm:push value="${targetModelName}Search" var="modelName" />
    <comm:push value="${targetFriendlyModelName} Search" var="friendlyModelName" />

    <c:set var="icon" value="${f:smd(targetModelName).icon}" />
    <t:view-frame id="${viewId}" title="${friendlyModelName}" icon="${icon}" removable="false" hideRefresh="true" panelClass="panel-primary" cssClass="search-view" >
        <ctrl:form name="${viewId}-form">

            <jsp:doBody/>

            <comm:peek var="base" />
            <c:set var="criteriaPath" value='${f:subStrB4Last(base, ".")}' />
            <spring:message var="literal_search" code="jsp.literal.search" />
            <spring:message var="literal_reset" code="jsp.literal.reset" />
            <div class="form-group">
                <div class="col-xs-offset-0 col-xs-5 col-sm-offset-3 col-sm-2">
                    <button type="submit" class="btn btn-primary"
                            data-spg-list='{"modelName":"${targetModelName}", "criteriaPath":"${criteriaPath}"}'>${literal_search}</button>
                </div>
                <div class="col-xs-offset-0 col-xs-7 col-sm-offset-1 col-sm-6">
                    <button type="button" class="btn btn-primary" data-ng-click="reset()" >${literal_reset}</button>
                </div>
            </div>
        </ctrl:form>
    </t:view-frame>

    <comm:pop var="targetModelName" />
    <comm:pop var="targetFriendlyModelName" />
    <comm:pop var="modelName" />
    <comm:pop var="friendlyModelName" />
    <comm:pop var="viewId" />
</t:view>