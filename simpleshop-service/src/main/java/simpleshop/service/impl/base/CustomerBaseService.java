

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.CustomerDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.service.CustomerService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class CustomerBaseService extends ModelServiceImpl<Customer, CustomerSearch> implements CustomerService {

    @Autowired
    protected CustomerDAO customerDAO;

    @Override
    protected ModelDAO<Customer> getModelDAO() {
        return customerDAO;
    }

    @Override
    public Customer create() {
        return new Customer();
    }

}
