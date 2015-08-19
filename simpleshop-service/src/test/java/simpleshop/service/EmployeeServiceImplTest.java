package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.PageInfo;
import simpleshop.data.SuburbDAO;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.Employee;
import simpleshop.domain.model.Suburb;
import simpleshop.domain.model.component.Address;
import simpleshop.dto.EmployeeSearch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class EmployeeServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private EmployeeService employeeService;

    @Before
    public void cleanUp(){
        super.cleanUp(employeeService, TestConstants.CUSTOMER_MARK);
    }

    @Autowired
    private SuburbDAO suburbDAO;

    private List<Employee> createTestEmployees(List<Suburb> suburbs){

        LocalDate[] hiredDates = {LocalDate.of(2000,4,5), LocalDate.of(2007,8,17), LocalDate.of(2012,10,3), LocalDate.of(2015,1,11)};

        List<Employee> employees = new ArrayList<>();
        for(int i=0; i<10; i++){
            Employee employee = new Employee();
            employee.getContact().setName("Employee " + (i % 2 == 0));
            employee.getContact().setContactName(TestConstants.CUSTOMER_MARK + i % 5);
            Address address = new Address();
            address.setSuburb(suburbs.get(i % suburbs.size()));
            address.setAddressLine1(i + (i % 2 == 0 ? "John St." : "Catherine St."));
            employee.getContact().setAddress(address);
            employee.setHireDate(hiredDates[i % hiredDates.length]);
            employeeService.save(employee);
            employees.add(employee);
        }
        return employees;
    }

    @Test
    public void searchTest() {

        List<Suburb> suburbs = suburbDAO.quickSearch("", new PageInfo(0, 10));
        assertThat(suburbs.size(), greaterThanOrEqualTo(3));
        suburbs = suburbs.subList(0,3);
        List<Employee> testEmployees = createTestEmployees(suburbs);
        flush();

        //the default search contains all test employees
        EmployeeSearch employeeSearch = new EmployeeSearch();
        employeeSearch.setPageSize(100);
        List<Employee> result = employeeService.search(employeeSearch);
        assertThat(result.size(), greaterThanOrEqualTo(testEmployees.size()));
        for(Employee testEmployee : testEmployees){
            assertThat(result.contains(testEmployee), equalTo(true));
        }

        //paging should work
        employeeSearch.setPageSize(5);
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(5));

        //search by name should work
        employeeSearch.setName(TestConstants.CUSTOMER_MARK);
        employeeSearch.setPageSize(20);
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(10));

        //second page
        employeeSearch.setPageSize(6);
        employeeSearch.setPageIndex(1);
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(4));

        //search by address
        employeeSearch.setPageIndex(0);
        employeeSearch.setAddress("John St.");
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(5));

        //search by suburb
        employeeSearch.setAddress(null);
        employeeSearch.setSuburb(suburbs.get(0));
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(4));

        employeeSearch.setSuburb(suburbs.get(1));
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(3));

        //search by hired date
        employeeSearch.setSuburb(null);
        employeeSearch.setHireDateLower(LocalDate.of(2010, 1, 1));
        employeeSearch.setPageSize(100);
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(4));

        //upper limit
        employeeSearch.setHireDateUpper(LocalDate.of(2015, 1, 1));
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(2));

        //search contact number
        testEmployees.get(0).getContact().getContactNumbers().put("email", "this_is_a_test@web.net");
        testEmployees.get(1).getContact().getContactNumbers().put("email", "this_is_a_test@abc.com");
        testEmployees.get(2).getContact().getContactNumbers().put("email", "this_is_a_test@web.net");
        flush();
        employeeSearch.setHireDateUpper(null);
        employeeSearch.setHireDateLower(null);
        employeeSearch.setContactNumber("test");
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(3));

        employeeSearch.setContactNumber("@web.net");
        result = employeeService.search(employeeSearch);
        assertThat(result.size(), equalTo(2));

    }

    @Test
    public void createDeleteTest(){

        Employee employee = employeeService.create();
        employee.getContact().setName("bbb" + TestConstants.CUSTOMER_MARK);

        employeeService.save(employee);

        assertThat(employee.getId(), notNullValue());
        employeeService.delete(employee);
        flush();

        employee = employeeService.getById(employee.getId());
        assertThat(employee, nullValue());
    }

}
