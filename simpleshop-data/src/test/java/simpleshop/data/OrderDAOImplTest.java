package simpleshop.data;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.Address;
import simpleshop.domain.model.component.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class OrderDAOImplTest extends TransactionalTest {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SuburbDAO suburbDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Before
    public void cleanUp() {
        cleanUp(orderDAO, TestConstants.ORDER_MARK);
    }

    @Test
    public void getTest(){
        assertThat(orderDAO.get(Integer.MAX_VALUE), nullValue());
    }

    @Test
    public void createDeleteTest() {

        List<Customer> customers = customerDAO.quickSearch(TestConstants.CUSTOMER_NAME_1, new PageInfo());
        assertThat(customers.size(), greaterThanOrEqualTo(1));

        List<Employee> employees = employeeDAO.quickSearch(TestConstants.EMPLOYEE_NAME_1, new PageInfo());
        assertThat(employees.size(), greaterThanOrEqualTo(1));

        List<Suburb> suburbs = suburbDAO.quickSearch(TestConstants.SUBURB_AUS_1, new PageInfo());
        assertThat(suburbs.size(), greaterThanOrEqualTo(1));

        Order order = new Order();
        order.setCustomer(customers.get(0));
        order.setEmployee(employees.get(0));
        order.setShipName(TestConstants.ORDER_MARK + "aa");
        LocalDateTime orderTime = LocalDateTime.of(2015, 8, 17, 16, 22, 32);
        order.setOrderDate(orderTime);
        Address shipAddress = new Address();
        shipAddress.setSuburb(suburbs.get(0));
        shipAddress.setAddressLine1("My Street Address");
        order.setShipAddress(shipAddress);
        List<Product> products = productDAO.quickSearch("", new PageInfo());
        assertThat(products.size(), greaterThanOrEqualTo(2));
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(products.get(0));
        orderItem.setSellPrice(new BigDecimal("11.40"));
        orderItem.setQuantity(1);
        order.getOrderItems().add(orderItem);

        orderDAO.save(order);
        orderDAO.sessionFlush();

        assertThat(order.getId(), not(nullValue()));
        orderDAO.evict(order);

        Order loaded = orderDAO.load(order.getId());
        assertThat(loaded.getCustomer(), sameInstance(order.getCustomer()));
        assertThat(loaded.getEmployee(), sameInstance(order.getEmployee()));
        assertThat(loaded.getRequiredDate(), nullValue());
        assertThat(loaded.getOrderDate(), equalTo(orderTime));
        assertThat(loaded.getOrderItems().size(), equalTo(1));

        loaded.setShippedDate(orderTime.plusDays(3));
        loaded.setFreight(new BigDecimal("15.00"));
        orderDAO.sessionFlush();

        orderDAO.delete(loaded);
    }

    @Test
    public void quickSearchTest() {
        List<Order> orders = orderDAO.quickSearch(TestConstants.CUSTOMER_NAME_1, new PageInfo());
        assertThat(orders.size(), greaterThanOrEqualTo(1));
        assertThat(orders.get(0).getCustomer().getContact().getName(), equalTo(TestConstants.CUSTOMER_NAME_1));

        Employee employee = orders.get(0).getEmployee();
        Shipper shipper = orders.get(0).getShipper();

        orders = orderDAO.quickSearch(TestConstants.CUSTOMER_NAME_2, new PageInfo());
        assertThat(orders.size(), equalTo(0));

        List<Customer> customers = customerDAO.quickSearch(TestConstants.CUSTOMER_NAME_2, new PageInfo());
        assertThat(customers.size(), greaterThanOrEqualTo(1));

        Customer customer = customers.get(0);

        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setCustomer(customer);
            order.setEmployee(employee);
            order.setShipName(TestConstants.ORDER_MARK + (i % 2 == 0 ? "_TypeA" : "_TypeB"));
            order.setShipper(shipper);
            orderDAO.save(order);
        }

        orders = orderDAO.quickSearch("_TypeA", new PageInfo(0, 10));
        assertThat(orders.size(), equalTo(5));

        orders = orderDAO.quickSearch(TestConstants.CUSTOMER_NAME_2, new PageInfo(0, 20));
        assertThat(orders.size(), equalTo(10));

        orders = orderDAO.quickSearch(TestConstants.CUSTOMER_NAME_2, new PageInfo(1, 7));
        assertThat(orders.size(), equalTo(3));

    }

}
