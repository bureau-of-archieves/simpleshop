package simpleshop.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.PageInfo;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;


import java.util.List;

import static org.junit.Assert.assertTrue;


public class CustomerServiceImplTest extends ServiceTransactionTest{

    @Autowired
    private CustomerService customerService;

    @Test
    public void searchTest(){
        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setName("a");
        List<Customer> customers = customerService.search(customerSearch);
        assertTrue(customers.size() > 0);
        for(Customer customer : customers){
            assertTrue(customer.getContact().getName().contains("a") || customer.getContact().getContactName().contains("a"));
        }
    }

    @Test
    public void quickSearchTest(){
        PageInfo pageInfo = new PageInfo();
        List<Customer> customers = customerService.quickSearch("Bill", pageInfo);
        assertTrue(customers.size() > 0);
        for(Customer customer : customers){
            assertTrue(customer.getContact().getName().contains("Bill") || customer.getContact().getContactName().contains("Bill")); //this is not exactly right for it will do for this test case.
        }
    }



}
