package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import simpleshop.domain.model.Order;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.service.OrderService;
import simpleshop.service.impl.base.OrderBaseService;

import javax.validation.constraints.NotNull;

@Service
public class OrderServiceImpl extends OrderBaseService implements OrderService {

    @Override
    protected void initialize(@NotNull Order model) {
        orderDAO.initialize(model.getOrderItems());
    }

    @Override
    public OrderItem createOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        return orderItem;
    }
}
