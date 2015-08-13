package simpleshop.data;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.OrderItem;
import java.math.BigDecimal;
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
    private OrderDAO orderDAO;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Order createOrder(){
        Customer customer = customerDAO.quickSearch("Google", new PageInfo()).get(0);
        Employee employee = employeeDAO.quickSearch("Steve", new PageInfo()).get(0);
        Product product = productDAO.quickSearch("Race Car", new PageInfo()).get(0);

        Order order = new Order();
        order.setCustomer(customer);
        order.setEmployee(employee);
        orderDAO.save(order);

        order.setShipName("John");
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setSellPrice(BigDecimal.ONE);
        order.getOrderItems().add(orderItem);

        return order;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void deleteOrder(Order order){
        orderDAO.delete(order);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createOrderTest(){

        Order order = createOrder();

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Customer.class);
        Criteria criteria2 = criteria.createCriteria("orders", "ord");
        criteria2.add(Restrictions.idEq(session.getIdentifier(order)));

        List<Customer> customers = criteria.list();
        deleteOrder(order);

        assertThat(customers.size(), greaterThanOrEqualTo(1));
    }

    @Autowired
    private SessionFactory sessionFactory;


}
