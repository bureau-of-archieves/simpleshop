<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="d" tagdir="/WEB-INF/tags/domain" %>
<%@ taglib prefix="comm" tagdir="/WEB-INF/tags/common"  %>
<%@ taglib prefix="ctrl" tagdir="/WEB-INF/tags/controls"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:page>

    <spring:message var="literal_save" code="jsp.literal.save" />
    <spring:message var="literal_cancel" code="jsp.literal.cancel" />

  <t:view-create controllerName="dialogView" cssClass="hide-panel-heading" noButtons="true">
      <comm:peek var="base" />
      <comm:push value="${base}customer.contact." var="base" />
      <comm:push value="Contact" var="modelName" />

      <ctrl:editField path="contactName" required="true" />
      <d:address-form path="address" required="true" />

      <comm:pop var="base" />
      <comm:pop var="modelName" />

      <ctrl:editField path="email" />

      <comm:peek var="base" />
      <comm:push value="${base}order." var="base" />
      <comm:push value="Order" var="modelName" />

      <ctrl:editChildList path="orderItems" readonly="true" >
          <ctrl:editModelLink path="product" />
          <ctrl:editField path="quantity" readonly="true" />
          <ctrl:editField path="sellPrice" readonly="true" currency="true" />
      </ctrl:editChildList>
      <ctrl:editField path="freight" readonly="true" currency="true"/>

      <comm:pop var="base" />
      <comm:pop var="modelName" />

      <div class="form-group">
          <div class="col-sm-offset-3 col-sm-9">
              <button type="submit" class="btn btn-primary" data-ng-click="submit($event)">${literal_save}</button>
              <button type="submit" class="btn btn-primary" data-ng-click="cancel()">${literal_cancel}</button>
          </div>
      </div>
  </t:view-create>

</t:page>

<%--override model type of the container--%>



