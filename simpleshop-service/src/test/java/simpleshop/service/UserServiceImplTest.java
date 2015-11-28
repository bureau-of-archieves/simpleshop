package simpleshop.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.User;

public class UserServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private UserService userService;

    @Test
    public void canFindUser(){
       User user = userService.findUser("zhy2003");
        Customer customer = user.getCustomer();

        System.out.println(customer);
        user.setCustomer(null);
        userService.saveUser(user);
    }
}
