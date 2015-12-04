package simpleshop.webapp.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.SortInfo;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.User;
import simpleshop.dto.CustomerSearch;
import simpleshop.dto.OrderSearch;
import simpleshop.service.CustomerService;
import simpleshop.service.UserService;

@Service
public class UserActionServiceImpl implements UserActionService {

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Transactional
    public OrderSearch createSearchUserOrders(){

        User user = securityContextService.getUser();
        if(user == null)
            return null;

        if(user.getCustomer() == null)
            return null;

        if(user.getCustomer().getId() == null)
            return null;

        Integer customerId = user.getCustomer().getId();
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setCustomer(new CustomerSearch());
        orderSearch.getCustomer().setId(customerId);

        SortInfo sortInfo = new SortInfo();
        sortInfo.setProperty("orderDate");
        sortInfo.setAscending(false);
        orderSearch.addSortInfo(sortInfo);
        return orderSearch;

    }

    @Transactional
    public Customer getCustomer() {
        User user = securityContextService.getUser();
        if(user == null)
            return null;

        Customer customer;
        if(user.getCustomer() == null){
            customer = customerService.create();
            customer.getContact().setName(user.getUsername());
            user.setCustomer(customer);
            userService.saveUser(user);

        } else {
            customer = customerService.getById(user.getCustomer().getId());
        }
        return customer;
    }
}
