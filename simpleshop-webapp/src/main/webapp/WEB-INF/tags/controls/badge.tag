<%@include file="../_header.tag" %>
<%--represents a badge--%>

<%@attribute name="value" required="true" %>
<%@attribute name="ngShow" %>

<span class="badge"
      <c:if test="${not empty ngShow}">
          data-ng-show="${ngShow}"
      </c:if> >${value}</span>