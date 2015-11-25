package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.User;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Unit tests for <code>UserDAOImpl</code>.
 */
public class UserDAOImplTest extends TransactionalTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Test
    public void getTest() {
        User user = userDAO.get(TestConstants.ADMIN_USER);
        assertThat(user, notNullValue());
        assertThat(user.getUsername(), equalTo(TestConstants.ADMIN_USER));
    }

    @Test
    public void createTest(){

        List<Customer> customerList = customerDAO.quickSearch(TestConstants.CUSTOMER_NAME_1, new PageInfo());
        assertThat(customerList.size(), greaterThan(0));

        Customer customer = customerList.get(0);

        User user = new User();
        user.setUsername(TestConstants.USER_MARK + "_1");
        user.setPassword("guess what");
        user.setCustomer(customer);

        userDAO.save(user);

        userDAO.sessionFlush();
    }


    @Before
    public void cleanUp() {
        cleanUp(userDAO, TestConstants.USER_MARK);
    }

}
