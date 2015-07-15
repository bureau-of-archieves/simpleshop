package simpleshop.domain.model;

import org.hibernate.annotations.BatchSize;
import simpleshop.Constants;
import simpleshop.domain.metadata.ItemText;
import simpleshop.domain.metadata.ItemValue;
import simpleshop.domain.metadata.Summary;
import simpleshop.domain.metadata.ValueClass;
import simpleshop.domain.model.component.Address;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name = "contacts")
public class Contact {

    private Integer id;
    private String name;
    private String contactName;
    private Map<String, String> contactNumbers = new TreeMap<>();
    private Address address = new Address();
    private String note;

    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    @ItemValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Summary
    @Column(length = Constants.MID_STRING_LENGTH, nullable = false)
    @NotNull
    @ItemText
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Summary
    @Column(name = "contact_name",  length = Constants.MID_STRING_LENGTH)
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Summary
    @ElementCollection
    @CollectionTable(name = "contact_numbers", joinColumns=@JoinColumn(name="contact_id"))
    @MapKeyColumn(name = "contact_type")
    @Column(name = "contact_number")
    @BatchSize(size = Constants.BATCH_SIZE)
    @MapKeyClass(String.class)
    @ValueClass()
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
