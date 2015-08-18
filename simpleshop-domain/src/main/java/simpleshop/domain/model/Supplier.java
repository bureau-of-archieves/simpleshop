package simpleshop.domain.model;

import org.hibernate.annotations.Cascade;
import simpleshop.domain.metadata.Description;
import simpleshop.domain.metadata.DisplayFormat;
import simpleshop.domain.metadata.Icon;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "suppliers")
@Icon("tag")
@DisplayFormat("concat:'id':' - ':'contact.name'")
public class Supplier {

    private Integer id;
    private Contact contact  = new Contact();

    /**
     * There can be at most 1 supplier who is STOCK.
     * When this supplier appears in an order item the shop stock decrease.
     * //todo add a StockStorage domain object and change this flag to an id. A stock storage is both a buyer and a supplier.
     */
    private Boolean stock = Boolean.FALSE;

    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.MERGE})
    @JoinColumn(name = "contact_id", nullable = false, updatable = false)
    @NotNull
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Column(name = "stock", nullable = false)
    @NotNull
    @Description("True if this supplier represents stock")
    public Boolean getStock() {
        return stock;
    }

    public void setStock(Boolean stock) {
        this.stock = stock;
    }

}
