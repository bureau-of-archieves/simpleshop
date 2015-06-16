package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.CustomerDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.service.CustomerService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

@Service
public class CustomerServiceImpl extends ModelServiceImpl<Customer, CustomerSearch> implements CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    protected ModelDAO getModelDAO() {
        return customerDAO;
    }

    @Override
    public Customer create() {
        return new Customer();
    }

}
