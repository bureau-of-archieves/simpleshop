package simpleshop.service.impl;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import simpleshop.domain.model.Customer;
import simpleshop.service.CustomerService;
import simpleshop.service.impl.base.CustomerBaseService;

import javax.validation.constraints.NotNull;

@Service
public class CustomerServiceImpl extends CustomerBaseService implements CustomerService {

    @Override
    protected void initialize(@NotNull Customer model) {
        Hibernate.initialize(model.getOrders());
    }

}
