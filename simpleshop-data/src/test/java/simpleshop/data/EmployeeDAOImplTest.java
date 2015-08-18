package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.Constants;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.ContactNumberType;
import simpleshop.domain.model.Employee;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class EmployeeDAOImplTest extends TransactionalTest {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Test
    public void getTest(){
        assertThat(employeeDAO.get(Integer.MAX_VALUE), nullValue());
    }

    @Test
    public void createDeleteTest(){
        Employee employee = new Employee();
        employee.setContact(new Contact());
        employee.getContact().setName(TestConstants.JON_SNOW);
        employee.getContact().setContactName(TestConstants.EMPLOYEE_MARK);
        employee.getContact().getContactNumbers().put(ContactNumberType.HOME_PHONE.name(), TestConstants.HOME_PHONE_NUMBER_1);
        employeeDAO.save(employee);
        employeeDAO.evict(employee);
        employee = employeeDAO.load(employee.getId());

        assertEquals(TestConstants.JON_SNOW, employee.getContact().getName());
        assertTrue(employee.getContact().getContactNumbers().size() == 1);
        assertEquals(TestConstants.HOME_PHONE_NUMBER_1, employee.getContact().getContactNumbers().get(ContactNumberType.HOME_PHONE.name()));

        employeeDAO.delete(employee);
    }

    private void createEmployee(String name){
        Employee employee = new Employee();
        employee.setContact(new Contact());
        employee.getContact().setName(name);
        employee.getContact().setContactName(TestConstants.EMPLOYEE_MARK);
        employeeDAO.save(employee);
    }

    @Test
    public void quickSearchTest(){
       for(int i=1; i<=10; i++){
           createEmployee("quickSearch - Employee" + i);
       }

        List<Employee> result = employeeDAO.quickSearch("Employee", new PageInfo());
        assertEquals(Constants.DEFAULT_PAGE_SIZE, result.size());
        for(Employee employee : result){
            assertThat(employee.getContact().getName(), containsString("Employee"));
        }

        result = employeeDAO.quickSearch("Employee", new PageInfo(0, 5));
        assertThat(result.size(), equalTo(5));

        result = employeeDAO.quickSearch("Employee", new PageInfo(1, 5));
        assertThat(result.size(), equalTo(5));

        result = employeeDAO.quickSearch("quickSearch", new PageInfo(0, Short.MAX_VALUE));
        assertThat(result.size(), equalTo(10));

    }

    @Before
    public void cleanUp() {
        cleanUp(employeeDAO, TestConstants.EMPLOYEE_MARK);
    }

}
