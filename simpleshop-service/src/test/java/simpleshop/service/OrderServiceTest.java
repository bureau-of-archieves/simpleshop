package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.OrderDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.CustomerSearch;
import simpleshop.dto.EmployeeSearch;
import simpleshop.dto.OrderSearch;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for Order service.
 */
public class OrderServiceTest extends ServiceTransactionTest{

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SuburbService suburbService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDAO orderDAO;

    @Before
    public void cleanUp() {

        super.cleanUp(orderDAO, TestConstants.ORDER_MARK);
        orderService.sessionFlush();
    }

    @Transactional()
    @Test
    public void createDeleteTest(){

        List<Customer> customers = customerService.quickSearch(TestConstants.CUSTOMER_NAME_1, new PageInfo());
        assertThat(customers.size(), greaterThanOrEqualTo(1));

        List<Employee> employees = employeeService.quickSearch(TestConstants.EMPLOYEE_NAME_1, new PageInfo());
        assertThat(employees.size(), greaterThanOrEqualTo(1));

        List<Suburb> suburbs = suburbService.quickSearch(TestConstants.SUBURB_AUS_1, new PageInfo());
        assertThat(suburbs.size(), greaterThanOrEqualTo(1));

        List<Product> products = productService.quickSearch(TestConstants.PRODUCT_NAME_1, new PageInfo());
        assertThat(products.size(), greaterThanOrEqualTo(1));

        Order order = createOrder(customers.get(0), employees.get(0), suburbs.get(0), products.get(0));
        order.setCustomer(customers.get(0));
        order.setEmployee(employees.get(0));
        order.setShipName(TestConstants.ORDER_MARK);
        order.setOrderDate(LocalDateTime.of(2010, 11, 5, 16, 30));
        order.getShipAddress().setAddressLine1("My Address 1");
        order.getShipAddress().setSuburb(suburbs.get(0));

        OrderItem orderItem = orderService.createOrderItem();
        orderItem.setProduct(products.get(0));
        orderItem.setSellPrice(new BigDecimal("12.50"));
        order.getOrderItems().add(orderItem);

        order.setFreight(new BigDecimal("5.20"));

        orderService.save(order);
        orderService.sessionFlush();
    }



    private Order createOrder(Customer customer, Employee employee, Suburb suburb, Product product){
        Order order = orderService.create();
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setShipName(TestConstants.ORDER_MARK);
        order.setOrderDate(LocalDateTime.of(2010, 11, 5, 16, 30));
        order.getShipAddress().setAddressLine1("My Address 1");
        order.getShipAddress().setSuburb(suburb);

        OrderItem orderItem = orderService.createOrderItem();
        orderItem.setProduct(product);
        orderItem.setSellPrice(new BigDecimal("12.50"));
        order.getOrderItems().add(orderItem);

        order.setFreight(new BigDecimal("5.20"));
        return order;
    }


    @Transactional
    @Test
    public void searchTest(){

        List<Customer> customers = customerService.quickSearch("", new PageInfo());
        assertThat(customers.size(), greaterThanOrEqualTo(3));

        List<Employee> employees = employeeService.quickSearch("", new PageInfo());
        assertThat(employees.size(), greaterThanOrEqualTo(2));

        List<Suburb> suburbs = suburbService.quickSearch("", new PageInfo());
        assertThat(suburbs.size(), greaterThanOrEqualTo(2));

        List<Product> products = productService.quickSearch("", new PageInfo());
        assertThat(products.size(), greaterThanOrEqualTo(2));

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setShipName(TestConstants.ORDER_MARK);
        List<Order> result = orderService.search(orderSearch);
        assertThat(result.size(), equalTo(0));

        Customer customer1 = customers.get(0);
        Customer customer2 = customers.get(1);
        Product product1 = products.get(0);
        Suburb suburb1 = suburbs.get(0);
        Employee employee1 = employees.get(0);
        Employee employee2 = employees.get(1);

        Order order1 = createOrder(customer1, employee1, suburb1, product1);
        orderService.save(order1);
        Order order2 = createOrder(customer2, employee1, suburb1, product1);
        orderService.save(order2);
        flush();

        orderSearch.setCustomer(new CustomerSearch());
        orderSearch.getCustomer().setName(customer1.getContact().getName());
        result = orderService.search(orderSearch);
        assertThat(result.size(), equalTo(1));

        orderSearch.getCustomer().setName(customer2.getContact().getName());
        result = orderService.search(orderSearch);
        assertThat(result.size(), equalTo(1));

        orderSearch.setCustomer(null);
        orderSearch.setEmployee(new EmployeeSearch());
        orderSearch.getEmployee().setName(employee1.getContact().getName());
        result = orderService.search(orderSearch);
        assertThat(result.size(), equalTo(2));

        Order order3 = createOrder(customer1, employee2, suburb1, product1);
        orderService.save(order3);
        flush();
        result = orderService.search(orderSearch);
        assertThat(result.size(), equalTo(2));


    }
}
