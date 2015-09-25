package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.CustomerDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.SuburbDAO;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.Suburb;
import simpleshop.domain.model.component.Address;
import simpleshop.dto.CustomerSearch;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class CustomerServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerDAO customerDAO;

    @Before
    public void cleanUp(){
        super.cleanUp(customerDAO, TestConstants.CUSTOMER_MARK);
    }

    @Autowired
    private SuburbDAO suburbDAO;

    private List<Customer> createTestCustomers(List<Suburb> suburbs){

        String[] contactTypes = {"QQ", "Ph", "Fax", "Email"};
        String[] contactNumbers = {"11112222", "22223333", "33334444", "abc11114444@web.net"};

        List<Customer> customers = new ArrayList<>();
        for(int i=0; i<10; i++){
            Customer customer = new Customer();
            customer.getContact().setName("Customer " + (i % 2 == 0));
            customer.getContact().setContactName(TestConstants.CUSTOMER_MARK + i % 5);
            Address address = new Address();
            address.setSuburb(suburbs.get(i % suburbs.size()));
            address.setAddressLine1(i + (i % 2 == 0 ? "John St." : "Catherine St."));
            customer.getContact().setAddress(address);

            for(int j=0; j<4; j++){
                customer.getContact().getContactNumbers().put(contactTypes[j], contactNumbers[j] + "_" + i);
            }
            customerService.save(customer);
            customers.add(customer);
        }
        return customers;
    }

    @Test
    public void searchTest() {

        List<Suburb> suburbs = suburbDAO.quickSearch("", new PageInfo(0, 10));
        assertThat(suburbs.size(), greaterThanOrEqualTo(3));
        suburbs = suburbs.subList(0,3);
        List<Customer> testCustomers = createTestCustomers(suburbs);
        flush();

        //the default search contains all test customers
        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setPageSize(100);
        List<Customer> result = customerService.search(customerSearch);
        assertThat(result.size(), greaterThanOrEqualTo(testCustomers.size()));

        //paging should work
        customerSearch.setPageSize(5);
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(5));

        //search by name should work
        customerSearch.setName(TestConstants.CUSTOMER_MARK);
        customerSearch.setPageSize(20);
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(10));

        //second page
        customerSearch.setPageSize(6);
        customerSearch.setPageIndex(1);
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(4));

        //contact number should work
        customerSearch.setContactNumber("11112222");
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(4));

        //pin point one contact number
        customerSearch.setPageIndex(0);
        customerSearch.setPageSize(20);
        customerSearch.setContactNumber("11112222_1");
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(1));

        //like search
        customerSearch.setContactNumber("2233");
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(10));

        //search by address
        customerSearch.setAddress("John St.");
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(5));

        //search by suburb
        customerSearch.setAddress(null);
        customerSearch.setSuburb(suburbs.get(0));
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(4));

        customerSearch.setSuburb(suburbs.get(1));
        result = customerService.search(customerSearch);
        assertThat(result.size(), equalTo(3));
    }

    @Test
    public void createDeleteTest(){

        Customer customer = customerService.create();
        customer.getContact().setName("bbb" + TestConstants.CUSTOMER_MARK);
        customerService.save(customer);

        assertThat(customer.getId(), notNullValue());
        customerService.delete(customer);
        flush();

        customer = customerService.getById(customer.getId());
        assertThat(customer, nullValue());
    }

}
