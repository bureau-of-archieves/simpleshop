package simpleshop.data;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.common.ReflectionUtils;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.ContactNumberType;
import simpleshop.domain.model.Employee;

import java.util.List;

import static org.junit.Assert.*;

public class EmployeeDAOImplTest extends TransactionalTest {

    @Autowired
    private EmployeeDAO employeeDAO;

    @BeforeClass
    public static void init(){
    }

    @Test
    public void getNullTest(){
        Employee employee = employeeDAO.get(0x7FFFFFFF);
        assertNull(employee);
    }

    @Test
    public void createDeleteTest(){
        Employee employee = new Employee();
        employee.setContact(new Contact());
        employee.getContact().setName(TestConstants.JON_SNOW);
        employee.getContact().getContactNumbers().put(ContactNumberType.HOME_PHONE.name(), TestConstants.HOME_PHONE_NUMBER_1);
        employeeDAO.save(employee);
        employeeDAO.evict(employee);
        employee = employeeDAO.load(employee.getId());

        assertEquals(TestConstants.JON_SNOW, employee.getContact().getName());
        assertTrue(employee.getContact().getContactNumbers().size() == 1);
        assertEquals(TestConstants.HOME_PHONE_NUMBER_1, employee.getContact().getContactNumbers().get(ContactNumberType.HOME_PHONE.name()));

        employeeDAO.delete(employee);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void createEmployee(String name){
        Employee employee = new Employee();
        employee.setContact(new Contact());
        employee.getContact().setName(name);
        employeeDAO.save(employee);
    }

    @Test
    public void quickSearchTest(){
       for(int i=1; i<=10; i++){
           createEmployee("Employee" + i);
       }

        List<Employee> result = employeeDAO.quickSearch("Employee", new PageInfo());
        assertEquals(10, result.size());

        List<Employee> top5Result = employeeDAO.quickSearch("Employee", new PageInfo(0, 5));
        assertEquals(5, top5Result.size());

        result.forEach(employeeDAO::delete);

    }





}
