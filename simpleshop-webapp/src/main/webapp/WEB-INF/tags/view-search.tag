<%@include file="_header.tag" %>
<%--search view base tag.--%>

<t:view>

    <c:set var="targetModelName" value="${f:peek(stack, '_modelName')}"/>
    <c:set var="targetFriendlyModelName" value="${f:peek(stack, '_friendlyModelName')}"/>
    <c:set var="modelName" value="${targetModelName}Search" />
    <c:set var="friendlyModelName" value="${targetFriendlyModelName} Search" />
    <c:set var="viewType" value="${f:peek(stack, '_viewType')}" />
    <c:set var="viewId" value="${f:pascalNameToUrlName(targetModelName)}-${viewType}"/>

    ${f:_push(stack, "_targetModelName", targetModelName)}
    ${f:_push(stack, "_targetFriendlyModelName", targetFriendlyModelName)}
    ${f:_push(stack, "_modelName", modelName)}
    ${f:_push(stack, "_friendlyModelName", friendlyModelName)}
    ${f:_push(stack, "_viewId", viewId)}

    <t:view-frame id="${viewId}" title="${friendlyModelName}" icon="${f:smd(targetModelName).icon}" removable="false" hideRefresh="true" panelClass="panel-primary" cssClass="search-view" >
        <ctrl:form name="${viewId}-form">

            <jsp:doBody/>

            <c:set var="criteriaPath" value='${f:subStrB4Last(f:peek(stack, "_base"), ".")}' />
            <div class="form-group">
                <div class="col-xs-offset-0 col-xs-5 col-sm-offset-3 col-sm-2">
                    <button type="submit" class="btn btn-primary"
                            data-spg-list='{"modelName":"${targetModelName}", "criteriaPath":"${criteriaPath}"}'>Search</button>
                </div>
                <div class="col-xs-offset-0 col-xs-7 col-sm-offset-1 col-sm-6">
                    <button type="button" class="btn btn-primary" data-ng-click="reset()" >Reset</button>
                </div>
            </div>
        </ctrl:form>
    </t:view-frame>

    ${f:_pop(stack, "_targetModelName")}
    ${f:_pop(stack, "_targetFriendlyModelName")}
    ${f:_pop(stack, "_modelName")}
    ${f:_pop(stack, "_friendlyModelName")}
    ${f:_pop(stack, "_viewId")}
</t:view>