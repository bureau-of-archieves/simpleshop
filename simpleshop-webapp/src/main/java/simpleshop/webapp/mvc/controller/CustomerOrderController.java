package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import simpleshop.data.CustomerDAO;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.dto.CartItem;
import simpleshop.dto.CustomerOrder;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ShoppingCart;
import simpleshop.service.CustomerService;
import simpleshop.service.OrderService;
import simpleshop.service.ProductService;
import simpleshop.service.UserService;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/customer_order")
public class CustomerOrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerDAO customerDAO;

    public static final String NO_SHOPPING_CART_FOUND = "Not shopping cart found";
    public static final String NO_SHOPPING_CART_ITEM_FOUND = "Not shopping cart item found";

    @Transactional(readOnly = true)
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public JsonResponse<CustomerOrder> create(HttpSession session) {

        ShoppingCart cart = (ShoppingCart) session.getAttribute(CartController.SHOPPING_CART_SESSION_KEY);
        if (cart == null) {
            return JsonResponse.createError(NO_SHOPPING_CART_FOUND, null);
        }
        if (cart.getItems().size() == 0) {
            return JsonResponse.createError(NO_SHOPPING_CART_ITEM_FOUND, null);
        }

        CustomerOrder customerOrder = new CustomerOrder();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return JsonResponse.createError("Not authenticated", null);
        }
        String username = auth.getName();
        User user = userService.findUser(username);
        if (user == null) {
            return JsonResponse.createError("Not user with name '" + username + "' found", null);
        }
        if (user.getCustomer() != null) {
            if (user.getCustomer().getContact().getContactName() == null) {
                user.getCustomer().getContact().setContactName(user.getCustomer().getContact().getName());
                customerDAO.evict(user.getCustomer());
            }

            customerOrder.setCustomer(user.getCustomer());
        } else {
            customerOrder.setCustomer(customerService.create());
        }


        Order order = new Order();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();

            Product product = productService.getById(cartItem.getProductId());
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSellPrice(BigDecimal.TEN); //todo decide price
            order.getOrderItems().add(orderItem);
        }
        order.setFreight(new BigDecimal("15.50")); //todo decide freight
        customerOrder.setOrder(order);
        return JsonResponse.createSuccess(customerOrder);
    }

    @Transactional()
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    public JsonResponse<String> process(@RequestBody CustomerOrder customerOrder) {

        customerOrder.getOrder().setOrderDate(LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return JsonResponse.createError("Not authenticated", null);
        }
        String username = auth.getName();
        User user = userService.findUser(username);
        if(user.getCustomer() == null){
            customerOrder.getCustomer().getContact().setName(customerOrder.getCustomer().getContact().getContactName());
            user.setCustomer(customerOrder.getCustomer());
            userService.saveUser(user);
        }

        Order order = customerOrder.getOrder();
        Customer customer = user.getCustomer();
        order.setCustomer(customer);
        order.setShipAddress(customerOrder.getCustomer().getContact().getAddress());
        Employee defaultEmployee = new Employee();
        defaultEmployee.setId(1);
        order.setEmployee(defaultEmployee);
        customerService.save(customer);
        order.setCountry(customer.getContact().getAddress().getSuburb().getCountry());
        orderService.save(order);

        return JsonResponse.createSuccess("OK");
    }
}
