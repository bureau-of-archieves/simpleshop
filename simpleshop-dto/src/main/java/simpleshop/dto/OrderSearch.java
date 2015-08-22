package simpleshop.dto;


import simpleshop.data.metadata.PropertyFilter;

public class OrderSearch extends ModelSearch {

    private CustomerSearch customer;
    private EmployeeSearch employee;
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
}
