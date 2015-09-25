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


}
