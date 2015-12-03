package simpleshop.domain.model.component;


import org.hibernate.validator.constraints.URL;
import simpleshop.Constants;
import simpleshop.domain.infrastructure.LocalDateTimePersistenceConverter;
import simpleshop.domain.metadata.Description;
import simpleshop.domain.metadata.DisplayFormat;
import simpleshop.domain.model.Supplier;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Embeddable
public class ProductSupplier {

    private Supplier supplier;
    private BigDecimal unitPrice;
    private String url;
    private LocalDateTime outOfStockDate;
    private String note;
    private LocalDateTime unitPriceDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "supplier_id", nullable = false, updatable = false)
    @NotNull //only this column will be used as a part of the composite key as it is not null.
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Column(name = "sell_price", precision = Constants.CURRENCY_PRECISION, scale = Constants.CURRENCY_SCALE)
    @DecimalMin(value = "0.0", inclusive = false)
    @DisplayFormat("currency")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @URL
    @Column(name = "url", length = Constants.VERY_LONG_STRING_LENGTH)
    @Description("Product details page on the supplier's website")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @DisplayFormat("| na")
    @Column(name = "out_of_stock_date")
    @Description("The last time this product was out of stock at the supplier")
    public LocalDateTime getOutOfStockDate() {
        return outOfStockDate;
    }

    public void setOutOfStockDate(LocalDateTime outOfStockDate) {
        this.outOfStockDate = outOfStockDate;
    }

    @Column(name = "note", length = Constants.VERY_LONG_STRING_LENGTH)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @DisplayFormat("| na")
    @Column(name = "unit_price_date")
    @Description("The last time this record was updated.")
    public LocalDateTime getUnitPriceDate() {
        return unitPriceDate;
    }

    public void setUnitPriceDate(LocalDateTime lastUpdated) {
        this.unitPriceDate = lastUpdated;
    }
}
