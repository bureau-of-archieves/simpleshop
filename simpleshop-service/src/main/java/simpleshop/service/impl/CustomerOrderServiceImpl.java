package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.CustomerDAO;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.domain.model.type.ContactNumberType;
import simpleshop.dto.*;
import simpleshop.service.*;
import simpleshop.service.infrastructure.SpongeRuntimeException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService{

    public static final String NO_SHOPPING_CART_FOUND = "Not shopping cart found";
    public static final String CART_ITEM_HAS_NO_PRODUCT_ID = "Cart item must have a product id";
    public static final String USER_NOT_FOUND = "No user with name '%s' found";

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmployeeService employeeService;

    @Transactional(readOnly = true)
    @Override
    public CustomerOrder create(String username, ShoppingCart cart) {

        User user = userService.findUser(username);
        if (user == null) {
            throw new SpongeRuntimeException(String.format(USER_NOT_FOUND, username));
        }

        Customer customer = user.getCustomer();
        if(customer != null){
            if(customer.getContact().getContactName() == null){
                customerDAO.evict(customer); //do not persist this change
                customer.getContact().setContactName(customer.getContact().getName());
            }
        } else {
            customer = customerService.create();
        }
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomer(customer);

        Order order = new Order();
        Map<Integer, Product> productMap = getCartProducts(cart);
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            Product product = productMap.get(cartItem.getProductId());
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSellPrice(BigDecimal.TEN); //todo decide price
            order.getOrderItems().add(orderItem);
        }
        order.setFreight(new BigDecimal("15.50")); //todo decide freight
        customerOrder.setOrder(order);
        return customerOrder;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Integer, Product> getCartProducts(ShoppingCart cart) {
        if(cart == null)
            throw new IllegalArgumentException(NO_SHOPPING_CART_FOUND);

        Map<Integer, Product> productMap = new HashMap<>();
        if(cart.getItems() == null || cart.getItems().size() == 0){
            return productMap;
        }

        ArrayList<Integer> productIds = new ArrayList<>();
        for(CartItem cartItem : cart.getItems()){
            if(cartItem == null || cartItem.getProductId() == null){
                throw new IllegalArgumentException(CART_ITEM_HAS_NO_PRODUCT_ID);
            }
            productIds.add(cartItem.getProductId());
        }

        ProductSearch productSearch = new ProductSearch();
        productSearch.setProductIds(productIds);
        productSearch.setPageSize(Integer.MAX_VALUE);
        List<Product> products = productService.search(productSearch);
        for(Product product : products){
            productMap.put(product.getId(), product);
        }
        return productMap;
    }

    @Transactional
    @Override
    public Order process(String username, CustomerOrder customerOrder) {
        Customer customer = customerOrder.getCustomer();
        User user = userService.findUser(username);
        if(user.getCustomer() == null){
            customer.getContact().setName(customer.getContact().getContactName());
            if(customerOrder.getEmail() != null){
                customer.getContact().getContactNumbers().put(ContactNumberType.EMAIL.toString(), customerOrder.getEmail());
            }
            user.setCustomer(customerOrder.getCustomer());
            userService.saveUser(user);
        }

        //todo for convenience I'm get the order object from request but the data should come from session for security reasons.
        Order order = customerOrder.getOrder();
        order.setOrderDate(LocalDateTime.now());
        order.setShipAddress(customer.getContact().getAddress());
        order.setShipName(customer.getContact().getContactName());
        order.setCountry(customer.getContact().getAddress().getSuburb().getCountry());

        customer = user.getCustomer();
        order.setCustomer(customer);

        order.setEmployee(employeeService.getDefaultEmployee());

        orderService.save(order);
        return order;
    }
}
