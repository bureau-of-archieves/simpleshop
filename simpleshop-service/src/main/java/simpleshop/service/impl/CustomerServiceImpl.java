package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.CustomerDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.service.CustomerService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

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


}
