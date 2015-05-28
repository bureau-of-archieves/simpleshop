package simpleshop.domain.model.component;

import simpleshop.Constants;
import simpleshop.domain.model.Product;
import simpleshop.domain.model.Supplier;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Embeddable
public class OrderItem {

    private Product product;
    private BigDecimal buyPrice;
    private Supplier supplier;
    private Integer quantity;
    private BigDecimal sellPrice;
    private String paymentMethod;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    @NotNull
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "quantity", nullable = false)
    @NotNull
    @Min(1)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "sell_price", nullable = false, precision = Constants.CURRENCY_PRECISION, scale = Constants.CURRENCY_SCALE)
    @NotNull
    @DecimalMin(value = "0.0")
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    //below are order result properties

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Column(name = "buy_price", precision = Constants.CURRENCY_PRECISION, scale = Constants.CURRENCY_SCALE)
    @DecimalMin(value = "0.0")
    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    @Column(name = "payment_method", length = Constants.MID_STRING_LENGTH)
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
