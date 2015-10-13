package simpleshop.dto;

import javax.validation.constraints.NotNull;

/**
 * Item in shopping cart.
 */
public class CartItem {

    private Integer productId;
    private Integer quantity = 1;

    @NotNull
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @NotNull
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
