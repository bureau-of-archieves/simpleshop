package simpleshop.service.impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.CustomerDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.service.CustomerService;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Service
public class CustomerServiceImpl extends ContactServiceImpl<Customer, CustomerSearch> implements CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    protected ModelDAO<Customer> getModelDAO() {
        return customerDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer create() {
        return new Customer();
    }

    @Override
    protected void initialize(@NotNull Customer model) {
        Hibernate.initialize(model.getOrders());
    }

    private static final String[] IGNORED_JSON_PROPERTIES = {"customer"};

    @Override
    public String[] ignoredJsonProperties() {
        return IGNORED_JSON_PROPERTIES;
    }
}
