package simpleshop.dto;


import org.hibernate.validator.constraints.Email;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.Order;

import javax.validation.constraints.NotNull;

public class CustomerOrder {

    private Customer customer;
    private String email;
    private Order order;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Email
    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
