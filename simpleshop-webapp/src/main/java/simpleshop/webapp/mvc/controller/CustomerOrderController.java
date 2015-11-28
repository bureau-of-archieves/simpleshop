package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import simpleshop.domain.model.*;
import simpleshop.dto.CustomerOrder;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ShoppingCart;
import simpleshop.service.CustomerOrderService;
import simpleshop.service.CustomerService;
import simpleshop.service.OrderService;
import simpleshop.service.UserService;
import simpleshop.webapp.infrastructure.SecurityContextService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;


@RestController
@RequestMapping("/customer_order")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private SecurityContextService securityContextService;

    public static final String NO_SHOPPING_CART_FOUND = "Not shopping cart found";
    public static final String NO_SHOPPING_CART_ITEM_FOUND = "Not shopping cart item found";
    public static final String NOT_AUTHENTICATED = "Not authenticated";

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public JsonResponse<Map<Integer, Product>> getCartProducts(HttpSession session){
        ShoppingCart cart = (ShoppingCart) session.getAttribute(CartController.SHOPPING_CART_SESSION_KEY);
        if (cart == null) {
            return JsonResponse.createError(NO_SHOPPING_CART_FOUND, null);
        }

        return JsonResponse.createSuccess(customerOrderService.getCartProducts(cart));
    }

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

        Authentication auth = securityContextService.get().getAuthentication();
        if (auth == null) {
            return JsonResponse.createError(NOT_AUTHENTICATED, null);
        }
        String username = auth.getName();
        try {
            CustomerOrder customerOrder = customerOrderService.create(username, cart);
            return JsonResponse.createSuccess(customerOrder);
        }catch (Exception ex){
            return JsonResponse.createError(ex.getLocalizedMessage(), null);
        }
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    public JsonResponse<Order> process(@RequestBody CustomerOrder customerOrder, HttpSession session) {

        customerOrder.getOrder().setOrderDate(LocalDateTime.now());
        Authentication auth = securityContextService.get().getAuthentication();
        if (auth == null) {
            return JsonResponse.createError(NOT_AUTHENTICATED, null);
        }
        String username = auth.getName();
        Order order = customerOrderService.process(username, customerOrder);
        session.removeAttribute(CartController.SHOPPING_CART_SESSION_KEY);
        return JsonResponse.createSuccess(order);
    }
}
