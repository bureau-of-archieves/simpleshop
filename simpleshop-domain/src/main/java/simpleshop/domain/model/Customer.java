package simpleshop.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;
import simpleshop.domain.metadata.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "customers")
@Icon("road")
@DisplayFormat("concat:'id':' - ':'contact.name'")
public class Customer {

    private Integer id;
    private Contact contact = new Contact();
    private List<Order> orders = new ArrayList<>();

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

    @JsonIgnoreProperties("customer")
    @OneToMany(mappedBy = "customer")
    @OrderBy("orderDate")
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
