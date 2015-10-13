package simpleshop.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * A shopping cart.
 */
public class ShoppingCart {

    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}