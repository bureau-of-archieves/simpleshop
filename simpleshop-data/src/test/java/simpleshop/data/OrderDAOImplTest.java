package simpleshop.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.*;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.OrderItem;

import java.math.BigDecimal;

import static org.junit.Assert.assertNull;

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
    public void createOrderTest(){

        Order order = createOrder();
        deleteOrder(order);
    }

}
