<%-- Standard view form control.--%>
<%@tag trimDirectiveWhitespaces="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--########################## ATTRIBUTES ################################--%>
<%--name used to refer to this form in AngularJs.--%>
<%@attribute name="name" required="true" %>
<%-- If this form is used to edit an model object or set search parameters. --%>
<%@attribute name="editForm" type="java.lang.Boolean" %>

<%--########################## TAG CONTENT ################################--%>
<comm:peek var="viewId" />
<div class="col-xs-11">
    <form name="${name}" class="form-horizontal spg-form" role="form" novalidate>

        <jsp:doBody/>

        <spring:message var="literal_save" code="jsp.literal.save" />
        <spring:message var="literal_cancel" code="jsp.literal.cancel" />
        <spring:message var="literal_reset" code="jsp.literal.reset" />
        <c:if test="${editForm}">
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <button type="submit" class="btn btn-primary" data-ng-disabled="isUnchanged()" data-spg-save="${viewId}">${literal_save}</button>
                    <button type="submit" class="btn btn-primary" data-spg-cancel="${viewId}">${literal_cancel}</button>
                    <button type="submit" class="btn btn-primary" data-ng-disabled="isUnchanged()" data-ng-click="reset()">${literal_reset}</button>
                </div>
            </div>
        </c:if>

    </form>
</div>
