package simpleshop.dto;


import simpleshop.data.metadata.PropertyFilter;

import java.time.LocalDateTime;

public class OrderSearch extends ModelSearch {

    private CustomerSearch customer;
    private EmployeeSearch employee;
    private LocalDateTime orderDateLower;
    private LocalDateTime orderDateUpper;
    private String shipName;

    @PropertyFilter(operator = PropertyFilter.Operator.LIKE)
    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    @PropertyFilter(operator = PropertyFilter.Operator.MATCH)
    public CustomerSearch getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerSearch customer) {
        this.customer = customer;
    }

    @PropertyFilter(operator = PropertyFilter.Operator.MATCH)
    public EmployeeSearch getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSearch employee) {
        this.employee = employee;
    }

    @PropertyFilter(property = "orderDate", operator = PropertyFilter.Operator.LESS, negate = true)
    public LocalDateTime getOrderDateLower() {
        return orderDateLower;
    }

    public void setOrderDateLower(LocalDateTime orderDateLower) {
        this.orderDateLower = orderDateLower;
    }

    @PropertyFilter(property = "orderDate", operator = PropertyFilter.Operator.GREATER, negate = true)
    public LocalDateTime getOrderDateUpper() {
        return orderDateUpper;
    }

    public void setOrderDateUpper(LocalDateTime orderDateUpper) {
        this.orderDateUpper = orderDateUpper;
    }

}
