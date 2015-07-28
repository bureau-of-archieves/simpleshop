package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.data.OrderDAO;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Order;

import java.io.Serializable;

@Repository
public class OrderDAOImpl extends ModelDAOImpl<Order> implements OrderDAO {

    @Override
    public Order load(Serializable id) {
        return super.load(Order.class, id);
    }

    @Override
    public Order get(Serializable id) {
        return super.get(Order.class, id);
    }
    
}
