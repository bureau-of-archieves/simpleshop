package simpleshop.service;


import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.service.infrastructure.ModelService;

public interface CustomerService extends ModelService<Customer, CustomerSearch> {

}
