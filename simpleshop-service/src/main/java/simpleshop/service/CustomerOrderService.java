package simpleshop.service;

import simpleshop.domain.model.Order;
import simpleshop.domain.model.Product;
import simpleshop.dto.CustomerOrder;
import simpleshop.dto.ShoppingCart;

import java.util.Map;

/**
 * Customer order related operations.
 */
public interface CustomerOrderService {

    CustomerOrder create(String username, ShoppingCart cart);

    Map<Integer, Product> getCartProducts(ShoppingCart cart);

    Order process(String username, CustomerOrder customerOrder);

}
