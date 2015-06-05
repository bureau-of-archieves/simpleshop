package simpleshop.data;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.common.ReflectionUtils;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.data.util.DomainUtils;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Customer;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class CustomerDAOImplTest extends TransactionalTest {

    @Autowired
    private CustomerDAO customerDAO;

    private static ModelMetadata searchMetadata;
    private static ModelMetadata customerMetadata;

    @BeforeClass
    public static void init(){
        customerMetadata = DomainUtils.createModelMetadata(Customer.class);
        searchMetadata = DomainUtils.createModelMetadata(MyCustomerSearch.class);
        customerMetadata.getPropertyMetadataMap().get("contact").setReturnTypeMetadata(DomainUtils.createModelMetadata(Contact.class));
    }

    @Test
    public void getTest(){
        Customer customer = customerDAO.get(0x7FFFFFFF);
        assertNull(customer);
    }

    @Test
    public void loadTest(){
        Customer customer = new Customer();
        customer.setContact(new Contact());
        customer.getContact().setName("Rambo");
        customer.getContact().getContactNumbers().put("phone", "119");
        customerDAO.save(customer);
        customerDAO.evict(customer);
        customer = customerDAO.load(customer.getId());

        ReflectionUtils.setProperty(customer, "contact.contactName", "John Rambo");
        assertEquals("John Rambo", customer.getContact().getContactName());
    }

    @AliasDeclaration(propertyName = "contact", aliasName = "ct")
    public static class MyCustomerSearch extends PageInfo {

        private String name;
        private String contactName;
        private String nameOrContactName;

        @PropertyFilter(alias = "ct")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @PropertyFilter(alias = "ct", operator = PropertyFilter.Operator.LIKE)
        private String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        @PropertyFilter.List({@PropertyFilter(alias = "ct", property = "name"), @PropertyFilter(alias = "ct", property = "contactName")})
        public String getNameOrContactName() {
            return nameOrContactName;
        }

        public void setNameOrContactName(String nameOrContactName) {
            this.nameOrContactName = nameOrContactName;
        }
    }

    @Test
    public void searchTest(){
        MyCustomerSearch searchParameters = new MyCustomerSearch();
        searchParameters.setName("Bill Gates");
        List<Customer> customerList = customerDAO.search(searchMetadata, customerMetadata, searchParameters);
        assertTrue(customerList.size() > 0);
        assertEquals("Bill Gates", customerList.get(0).getContact().getName());

        Customer bill = customerList.get(0);
        bill.getContact().setContactName("Test contact name.");
        customerDAO.save(bill);
        customerDAO.sessionFlush();

        searchParameters.setContactName("Test%");
        customerList = customerDAO.search(searchMetadata, customerMetadata, searchParameters);
        assertTrue(customerList.size() > 0);
        assertTrue(customerList.get(0).getContact().getContactName().startsWith("Test"));

        searchParameters = new MyCustomerSearch();
        searchParameters.setNameOrContactName("Bill Gates");
        customerList = customerDAO.search(searchMetadata, customerMetadata, searchParameters);
        assertTrue(customerList.size() >= 2);
        for(Customer customer : customerList){
            assertTrue("Bill Gates".equals(customer.getContact().getName()) || "Bill Gates".equals(customer.getContact().getContactName()));
        }
    }

    @Test
    public void searchPagingTest(){
        MyCustomerSearch customerSearch = new MyCustomerSearch();
        customerSearch.setSortInfoList(new ArrayList<>());
        SortInfo sortInfo = new SortInfo();
        sortInfo.setAlias("ct");
        sortInfo.setAscending(false);
        sortInfo.setProperty("name");
        customerSearch.getSortInfoList().add(sortInfo);

        List<Customer> customerList = customerDAO.search(searchMetadata, customerMetadata, customerSearch);
        assertTrue(customerList.size() >= 3);
        assertTrue(customerList.get(0).getContact().getName().compareTo(customerList.get(1).getContact().getName()) >= 0);
        assertTrue(customerList.get(1).getContact().getName().compareTo(customerList.get(2).getContact().getName()) >= 0);

        sortInfo.setAscending(true);
        customerList = customerDAO.search(searchMetadata, customerMetadata, customerSearch);
        assertTrue(customerList.size() >= 3);
        assertTrue(customerList.get(0).getContact().getName().compareTo(customerList.get(1).getContact().getName()) <= 0);
        assertTrue(customerList.get(1).getContact().getName().compareTo(customerList.get(2).getContact().getName()) <= 0);

        customerSearch.setPageSize(2);
        customerList = customerDAO.search(searchMetadata, customerMetadata, customerSearch);
        assertTrue(customerList.size() == 2);
    }



}
