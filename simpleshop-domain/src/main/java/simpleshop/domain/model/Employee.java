package simpleshop.domain.model;

import org.hibernate.annotations.Cascade;
import simpleshop.domain.metadata.DisplayFormat;
import simpleshop.domain.metadata.Icon;
import simpleshop.domain.metadata.Summary;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "employees")
@Icon("flash")
@DisplayFormat("concat:'id':' - ':'contact.name'")
public class Employee {

    private Integer id;
    private LocalDate hireDate;
    private EmploymentType employmentType;
    private List<Order> orders = new ArrayList<>();
    private Contact contact;

    @Id
    @GeneratedValue
    @Column(name="id", nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "hire_date")
    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "employment_type")
    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    @Summary
    @ManyToOne(optional = false)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REFRESH})
    @JoinColumn(name = "contact_id", nullable = false, updatable = false)
    @NotNull
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @OneToMany(mappedBy = "employee")
    @OrderBy("id")
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
