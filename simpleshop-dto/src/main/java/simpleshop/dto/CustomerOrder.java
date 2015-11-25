package simpleshop.dto;


import simpleshop.domain.model.Customer;
import simpleshop.domain.model.Order;

public class CustomerOrder {

    private Customer customer;
    private Order order;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
