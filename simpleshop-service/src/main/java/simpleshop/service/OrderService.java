package simpleshop.service;

import simpleshop.domain.model.Order;
import simpleshop.dto.OrderSearch;
import simpleshop.service.infrastructure.ModelService;

public interface OrderService extends ModelService<Order, OrderSearch> {

}
