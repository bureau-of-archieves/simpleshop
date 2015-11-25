<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="d" tagdir="/WEB-INF/tags/domain" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>

<t:page>
  <t:view-create>
      <comm:peek var="base" />
      <comm:push value="${base}customer.contact." var="base" />
      <comm:push value="Contact" var="modelName" />

      <ctrl:editField path="contactName" />
      <d:address-form path="address" />

      <comm:pop var="base" />
      <comm:pop var="modelName" />


      <comm:push value="${base}order." var="base" />
      <comm:push value="Order" var="modelName" />

      <ctrl:editChildList path="orderItems" >
          <ctrl:editModelLink path="product" />
          <ctrl:editField path="quantity" />
          <ctrl:editField path="sellPrice" />
          <ctrl:editModelLink path="supplier" />
      </ctrl:editChildList>
      <ctrl:editField path="freight" />

      <comm:pop var="base" />
      <comm:pop var="modelName" />

  </t:view-create>

</t:page>

<%--override model type of the container--%>



