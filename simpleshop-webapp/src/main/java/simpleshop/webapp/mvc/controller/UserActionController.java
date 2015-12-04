package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.Order;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.OrderSearch;
import simpleshop.webapp.infrastructure.UserActionService;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserActionController {

    @Autowired
    private OrderController orderController;

    @Autowired
    private UserActionService userActionService;

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public JsonResponse<Iterable<Order>> getOrders(){

        OrderSearch orderSearch = userActionService.createSearchUserOrders();
        if(orderSearch == null){
            return JsonResponse.createSuccess(new ArrayList<>());
        }

        return orderController.orderSearch(orderSearch, null);
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    JsonResponse<Customer> getCustomer(){
        Customer customer = userActionService.getCustomer();

        if(customer == null){
            return JsonResponse.createError(CustomerOrderController.NOT_AUTHENTICATED, null);
        } else {
            return JsonResponse.createSuccess(customer);
        }
    }


}
