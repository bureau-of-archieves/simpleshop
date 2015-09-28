package simpleshop.data;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.common.ReflectionUtils;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.data.util.DomainUtils;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.Address;
import simpleshop.domain.model.type.ContactNumberType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CustomerDAOImplTest extends TransactionalTest {

    @Autowired
    private CustomerDAO customerDAO;

    private static ModelMetadata searchMetadata;
    private static ModelMetadata customerMetadata;

    @BeforeClass
    public static void init() {
        customerMetadata = DomainUtils.createModelMetadata(Customer.class);
        searchMetadata = DomainUtils.createModelMetadata(MyCustomerSearch.class);
        customerMetadata.getPropertyMetadataMap().get("contact").setReturnTypeMetadata(DomainUtils.createModelMetadata(Contact.class));
    }

    @Test
    public void getTest() {
        Customer customer = customerDAO.get(0x7FFFFFFF);
        assertNull(customer);
    }

    @Test
    public void loadTest() {
        Customer customer = new Customer();
        customer.setContact(new Contact());
        customer.getContact().setName(TestConstants.RAMBO);
        customer.getContact().getContactNumbers().put(ContactNumberType.HOME_PHONE.name(), TestConstants.HOME_PHONE_NUMBER_2);
        customerDAO.save(customer);
        customerDAO.evict(customer);
        customer = customerDAO.load(customer.getId());

        assertNotNull(customer.getContact());
        assertEquals(TestConstants.RAMBO, customer.getContact().getName());
        assertNotNull(customer.getContact().getContactNumbers());
        assertEquals(TestConstants.HOME_PHONE_NUMBER_2, customer.getContact().getContactNumbers().get(ContactNumberType.HOME_PHONE.name()));

        ReflectionUtils.setProperty(customer, "contact.contactName", "John Rambo");
        assertEquals("John Rambo", customer.getContact().getContactName());

        customerDAO.delete(customer);
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
    public void searchTest() {
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
        for (Customer customer : customerList) {
            assertTrue("Bill Gates".equals(customer.getContact().getName()) || "Bill Gates".equals(customer.getContact().getContactName()));
        }
    }

    @Test
    public void searchPagingTest() {
        MyCustomerSearch customerSearch = new MyCustomerSearch();
        customerSearch.setSortInfoList(new ArrayList<>());
        SortInfo sortInfo = new SortInfo();
        sortInfo.setAlias("ct");
        sortInfo.setAscending(false);
        sortInfo.setProperty("name");
        customerSearch.getSortInfoList().add(sortInfo);

        List<Customer> customerList = customerDAO.search(searchMetadata, customerMetadata, customerSearch);
        assertTrue(customerList.size() >= 3);
        for(int i=1; i<customerList.size(); i++){
            assertThat(customerList.get(i-1).getContact().getName().toLowerCase(), greaterThanOrEqualTo(customerList.get(i).getContact().getName().toLowerCase()));
        }

        sortInfo.setAscending(true);
        customerList = customerDAO.search(searchMetadata, customerMetadata, customerSearch);
        assertTrue(customerList.size() >= 3);
        for(int i=1; i<customerList.size(); i++){
            assertThat(customerList.get(i-1).getContact().getName().toLowerCase(), lessThanOrEqualTo(customerList.get(i).getContact().getName().toLowerCase()));
        }

        customerSearch.setPageSize(2);
        customerList = customerDAO.search(searchMetadata, customerMetadata, customerSearch);
        assertTrue(customerList.size() == 2);
    }

    @Test
    public void quickSearchTest(){
        List<Customer> customers = customerDAO.quickSearch("Bill", new PageInfo());
        assertTrue(customers.size() > 0);

        customers = customerDAO.quickSearch(TestConstants.NO_ONE_HAS_THIS_NAME, new PageInfo());
        assertTrue(customers.size() == 0);
    }

    @Test
    public void createAndDeleteTest() {
        Integer customerId = createTestMan1();

        Customer customer = customerDAO.load(customerId);

        assertNotNull(customer.getContact());
        assertEquals(TestConstants.TEST_MAN_1_NAME, customer.getContact().getName());

        assertNotNull(customer.getContact().getAddress());
        assertNotNull(TestConstants.MAGIC_STREET_1, customer.getContact().getAddress().getAddressLine1());
        customerDAO.delete(customer);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Integer createTestMan1(){
        Customer customer = new Customer();
        customer.setContact(new Contact());
        customer.getContact().setName(TestConstants.TEST_MAN_1_NAME);
        customer.getContact().setContactNumbers(new HashMap<>());
        Address address = new Address();
        address.setAddressLine1(TestConstants.MAGIC_STREET_1);
        customer.getContact().setAddress(address);
        customerDAO.save(customer);
        return customer.getId();
    }

}
