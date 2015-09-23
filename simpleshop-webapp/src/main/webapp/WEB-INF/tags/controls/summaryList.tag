<%-- A list of summary (inline representation) of objects.--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%@attribute name="modelId" required="true" %>
<%@attribute name="modelName" required="false" %>

<%--########################## ATTRIBUTE DEFAULT VALUES  ################################--%>
<comm:peekIfEmpty var="modelName" value="${modelName}" />

<%--########################## TAG CONTENT ################################--%>
<comm:peek var="friendlyMmodelName" />
<comm:peek var="parentId" />
<comm:peek var="viewId" />
<c:set var="idPrefix" value="${parentId}-smylst-" />
<div class="row">
    <div class="col-sm-8">
        <h4><spring:message code="jsp.literal.searchResultFor" /> ${friendlyMmodelName}</h4>
    </div>
    <div class="col-sm-4 clearfix">
        <form class="form-inline pull-right" data-ng-show="sortProperties.length > 0">
            <div class="input-group">
                <c:set var="sortSelectId" value="${idPrefix}sortBy" />
                <label for="${sortSelectId}" class="input-group-addon" >
                    <spring:message code="jsp.literal.sortBy" />
                </label>
                <select id="${sortSelectId}" class="selectpicker form-control" data-ng-model="postData.sortInfo" data-ng-options="item.text for item in sortProperties">
                </select>
            </div>
        </form>
    </div>
</div>

<div class="list-group">
    <a href="javascript:void(0);" data-spg-details='{"modelName":"${modelName}","modelId":${modelId}}' class="list-group-item clearfix" data-ng-repeat="item in model">
        <jsp:doBody/>
        <button class="btn btn-sm btn-warning pull-right" data-ng-mouseover="$event.stopPropagation();" data-spg-delete='{"modelName":"${modelName}","modelId":${modelId}}'>
            <spring:message code="jsp.literal.delete" />
        </button>
    </a>
    <a class="list-group-item" data-ng-show="model.length == 0">
        <spring:message code="jsp.literal.noResultFound" />
    </a>
</div>

<div class="row">
    <div class="col-xs-6 col-sm-5">
        <button type="submit" class="btn btn-primary" data-spg-create="${modelName}">
            <spring:message code="jsp.literal.createNew" />
        </button>
    </div>
    <div class="col-xs-6 col-sm-7">
        <nav>
            <ul class="pager">
                <li class="previous {{previousEnabled()}}" >
                    <a href="#" data-spg-refresh='{"viewId":"${viewId}","pageDelta":-1}'><span aria-hidden="true">&larr; </span> <spring:message code="jsp.literal.prev" /></a>
                </li>
                <li class="next {{nextEnabled()}}" >
                    <a href="#" data-spg-refresh='{"viewId":"${viewId}","pageDelta":1}' ><spring:message code="jsp.literal.next" /> <span aria-hidden="true"> &rarr;</span></a>
                </li>
            </ul>
        </nav>
    </div>
</div>






