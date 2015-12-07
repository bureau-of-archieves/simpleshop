package simpleshop.domain.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import simpleshop.Constants;
import simpleshop.domain.metadata.AutoLoad;
import simpleshop.domain.model.component.Address;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "contacts")
public class Contact {

    private Integer id;
    private String name;
    private String contactName;
    private Map<String, String> contactNumbers = new HashMap<>();
    private Address address = new Address();
    private String note;

    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(length = Constants.MID_STRING_LENGTH, nullable = false)
    @NotNull
    @Size(min = 2)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "contact_name",  length = Constants.MID_STRING_LENGTH)
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @AutoLoad
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @MapKeyClass(String.class)
    @CollectionTable(name = "contact_numbers", joinColumns=@JoinColumn(name="contact_id"))
    @MapKeyColumn(name = "contact_type")
    @Column(name = "contact_number")
    @BatchSize(size = Constants.BATCH_SIZE)
    @Cascade({CascadeType.ALL})
    public Map<String, String> getContactNumbers() {
        return contactNumbers;
    }

    public void setContactNumbers(Map<String, String> contactNumbers) {
        this.contactNumbers = contactNumbers;
    }

    @Embedded
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Column(length = Constants.VERY_LONG_STRING_LENGTH)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
