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

    @Before
    public void cleanUp() {
        cleanUp(orderDAO, TestConstants.ORDER_MARK);
    }

    private Order createOrder(){
        Customer customer = customerDAO.quickSearch("Google", new PageInfo()).get(0);
        Employee employee = employeeDAO.quickSearch("Steve", new PageInfo()).get(0);
        Product product = productDAO.quickSearch("Race Car", new PageInfo()).get(0);

        Order order = new Order();
        order.setCustomer(customer);
        order.setEmployee(employee);
        orderDAO.save(order);

        order.setShipName("John " + TestConstants.ORDER_MARK);
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setSellPrice(BigDecimal.ONE);
        order.getOrderItems().add(orderItem);

        return order;
    }


    @Test
    @SuppressWarnings("unchecked")
    public void createOrderTest(){

        Order order = createOrder();

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Customer.class, "c");

        DetachedCriteria subQuery = DetachedCriteria.forClass(Order.class, "sub1");
        subQuery.setProjection(Property.forName("id"));
        //subQuery.add(Restrictions.idEq(25));
        DetachedCriteria subQuery2 = DetachedCriteria.forClass(Customer.class, "sub2").add(Restrictions.eqProperty("id", "c.id")).createCriteria("orders", "ord").setProjection(Property.forName("ord.id"));
        subQuery.add(Subqueries.propertyIn("id", subQuery2));
        //subQuery.add(Restrictions.eqProperty("customer", "c.id"));
        criteria.add(Subqueries.exists(subQuery));
        List<Customer> customers = criteria.list();
        assertThat(customers.size(), greaterThanOrEqualTo(1));

    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void accessEmbeddedPropertyTest() {
//        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Customer.class, "c");
//        Criteria criteria1 = criteria.createCriteria("contact", "ct");
//        Criteria criteria2 = criteria1.createCriteria("address.suburb", "sb");
//        criteria2.add(Restrictions.ilike("sb.suburb", "%wol%"));
//        List<Customer> customers = criteria.list();
//        assertThat(customers.size(), greaterThanOrEqualTo(1));
//    }
//
//    @Test
//    public void accessEmbeddedObjectPropertyTest(){
//
//
//
//        //criteria1.add(Restrictions.ilike("address.addressLine1", "%"));
//
////        Suburb suburb = new Suburb();
////        suburb.setId(1);
////        criteria1.add(Restrictions.eq("address.suburb", suburb));
//    }

    @Autowired
    private SessionFactory sessionFactory;


}
