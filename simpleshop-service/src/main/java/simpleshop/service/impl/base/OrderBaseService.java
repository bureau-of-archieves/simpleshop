

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.OrderDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Order;
import simpleshop.dto.OrderSearch;
import simpleshop.service.OrderService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class OrderBaseService extends ModelServiceImpl<Order, OrderSearch> implements OrderService {

    @Autowired
    protected OrderDAO orderDAO;

    @Override
    protected ModelDAO<Order> getModelDAO() {
        return orderDAO;
    }

    @Override
    public Order create() {
        return new Order();
    }

}
