package simpleshop.webapp.infrastructure;

import simpleshop.domain.model.Customer;
import simpleshop.dto.OrderSearch;

/**
 * User action service methods.
 */
public interface UserActionService {

    OrderSearch createSearchUserOrders();

    Customer getCustomer();
}
