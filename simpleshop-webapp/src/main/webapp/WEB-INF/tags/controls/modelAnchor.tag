<%@include file="../_header.tag" %>

<%--the model name of the model of this anchor --%>
<%@attribute name="modelName" required="true" %>
<%@attribute name="idExpr" required="true" %>
<%@attribute name="descExpr" required="true" %>

<%@attribute name="addon" type="java.lang.Boolean" required="false" %>


<c:if test="${addon}">
    <div class="input-group-addon" data-ng-show="linkRequests[0].modelName=='${modelName}'">
</c:if>
    <span class="glyphicon glyphicon-map-marker pull-right cursor-pointer"
          data-spg-end-link-request
          data-id="${idExpr}"
          data-desc="${descExpr}"
          <c:if test="${not addon}">
              data-ng-show="linkRequests[0].modelName=='${modelName}'"
          </c:if>
            ></span>
<c:if test="${addon}">
    </div>
</c:if>