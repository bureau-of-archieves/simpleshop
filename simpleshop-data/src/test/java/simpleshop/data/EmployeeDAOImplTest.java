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
import simpleshop.domain.model.Employee;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EmployeeDAOImplTest extends TransactionalTest {

    @Autowired
    private EmployeeDAO employeeDAO;

    //private static ModelMetadata searchMetadata;
    //private static ModelMetadata employeeMetadata;

    @BeforeClass
    public static void init(){
        //employeeMetadata = DomainUtils.createModelMetadata(Employee.class);
        //searchMetadata = DomainUtils.createModelMetadata(MyEmployeeSearch.class);
        //employeeMetadata.getPropertyMetadataMap().get("contact").setReturnTypeMetadata(DomainUtils.createModelMetadata(Contact.class));
    }

    @Test
    public void getTest(){
        Employee employee = employeeDAO.get(0x7FFFFFFF);
        assertNull(employee);
    }

    @Test
    public void loadTest(){
        Employee employee = new Employee();
        employee.setContact(new Contact());
        employee.getContact().setName("Jon Snow");
        employee.getContact().getContactNumbers().put("phone", "789");
        employeeDAO.save(employee);
        employeeDAO.evict(employee);
        employee = employeeDAO.load(employee.getId());

        assertEquals("Jon Snow", employee.getContact().getName());

        ReflectionUtils.setProperty(employee, "contact.contactName", "Jon Snow2");
        assertEquals("Jon Snow2", employee.getContact().getContactName());


    }

//    @AliasDeclaration(propertyName = "contact", aliasName = "ct")
//    public static class MyEmployeeSearch extends PageInfo {
//
//        private String name;
//        private String contactName;
//        private String nameOrContactName;
//
//        @PropertyFilter(alias = "ct")
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        @PropertyFilter(alias = "ct", operator = PropertyFilter.Operator.LIKE)
//        private String getContactName() {
//            return contactName;
//        }
//
//        public void setContactName(String contactName) {
//            this.contactName = contactName;
//        }
//
//        @PropertyFilter.List({@PropertyFilter(alias = "ct", property = "name"), @PropertyFilter(alias = "ct", property = "contactName")})
//        public String getNameOrContactName() {
//            return nameOrContactName;
//        }
//
//        public void setNameOrContactName(String nameOrContactName) {
//            this.nameOrContactName = nameOrContactName;
//        }
//    }
//
//    @Test
//    public void searchTest(){
//        MyEmployeeSearch searchParameters = new MyEmployeeSearch();
//        searchParameters.setName("Bill Gates");
//        List<Employee> employeeList = employeeDAO.search(searchMetadata, employeeMetadata, searchParameters);
//        assertTrue(employeeList.size() > 0);
//        assertEquals("Bill Gates", employeeList.get(0).getContact().getName());
//
//        Employee bill = employeeList.get(0);
//        bill.getContact().setContactName("Test contact name.");
//        employeeDAO.save(bill);
//        employeeDAO.sessionFlush();
//
//        searchParameters.setContactName("Test%");
//        employeeList = employeeDAO.search(searchMetadata, employeeMetadata, searchParameters);
//        assertTrue(employeeList.size() > 0);
//        assertTrue(employeeList.get(0).getContact().getContactName().startsWith("Test"));
//
//        searchParameters = new MyEmployeeSearch();
//        searchParameters.setNameOrContactName("Bill Gates");
//        employeeList = employeeDAO.search(searchMetadata, employeeMetadata, searchParameters);
//        assertTrue(employeeList.size() >= 2);
//        for(Employee employee : employeeList){
//            assertTrue("Bill Gates".equals(employee.getContact().getName()) || "Bill Gates".equals(employee.getContact().getContactName()));
//        }
//    }
//
//    @Test
//    public void searchPagingTest(){
//        MyEmployeeSearch employeeSearch = new MyEmployeeSearch();
//        employeeSearch.setSortInfoList(new ArrayList<>());
//        SortInfo sortInfo = new SortInfo();
//        sortInfo.setAlias("ct");
//        sortInfo.setAscending(false);
//        sortInfo.setProperty("name");
//        employeeSearch.getSortInfoList().add(sortInfo);
//
//        List<Employee> employeeList = employeeDAO.search(searchMetadata, employeeMetadata, employeeSearch);
//        assertTrue(employeeList.size() >= 3);
//        assertTrue(employeeList.get(0).getContact().getName().compareTo(employeeList.get(1).getContact().getName()) >= 0);
//        assertTrue(employeeList.get(1).getContact().getName().compareTo(employeeList.get(2).getContact().getName()) >= 0);
//
//        sortInfo.setAscending(true);
//        employeeList = employeeDAO.search(searchMetadata, employeeMetadata, employeeSearch);
//        assertTrue(employeeList.size() >= 3);
//        assertTrue(employeeList.get(0).getContact().getName().compareTo(employeeList.get(1).getContact().getName()) <= 0);
//        assertTrue(employeeList.get(1).getContact().getName().compareTo(employeeList.get(2).getContact().getName()) <= 0);
//
//        employeeSearch.setPageSize(2);
//        employeeList = employeeDAO.search(searchMetadata, employeeMetadata, employeeSearch);
//        assertTrue(employeeList.size() == 2);
//    }



}
