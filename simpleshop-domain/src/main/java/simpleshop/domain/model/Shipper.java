package simpleshop.domain.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import simpleshop.domain.metadata.Icon;
import simpleshop.domain.metadata.InterpolateFormat;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "shippers")
@Icon("plane")
@InterpolateFormat("{{id}} - {{contact.name}}")
public class Shipper {

    private Integer id;
    private Contact contact = new Contact();

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
    @Cascade({CascadeType.ALL})
    @JoinColumn(name = "contact_id", nullable = false, updatable = false)
    @NotNull
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
