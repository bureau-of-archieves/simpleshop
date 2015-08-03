package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.OrderDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Order;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.dto.OrderSearch;
import simpleshop.service.OrderService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

@Service
public class OrderServiceImpl extends ModelServiceImpl<Order, OrderSearch> implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Override
    protected ModelDAO getModelDAO() {
        return orderDAO;
    }

    @Override
    public Order create() {
        return new Order();
    }

    @Override
    public OrderItem createOrderItem(){
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        return orderItem;
    }

}
