package simpleshop.domain.model;

import org.hibernate.annotations.BatchSize;
import simpleshop.Constants;
import simpleshop.domain.metadata.Description;
import simpleshop.domain.metadata.DisplayFormat;
import simpleshop.domain.metadata.Icon;
import simpleshop.domain.metadata.Watermark;
import simpleshop.domain.model.component.Address;
import simpleshop.domain.model.component.OrderItem;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
@Icon("tasks")
public class Order {

    private Integer id;
    private Customer customer;
    private Employee employee;
    private LocalDateTime orderDate;
    private LocalDateTime requiredDate;
    private Country country;
    private List<OrderItem> orderItems = new ArrayList<>();

    //order result
    private Shipper shipper;
    private LocalDateTime shippedDate;
    private Integer numberOfParcels;
    private BigDecimal freight;
    private String shipName;
    private Address shipAddress = new Address();

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
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @NotNull
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Column(name = "order_date", nullable = false)
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @DisplayFormat("| na")
    @Description("The client what this order to be delivered on or before this date.")
    @Column(name = "required_date")
    public LocalDateTime getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(LocalDateTime requiredDate) {
        this.requiredDate = requiredDate;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_code", nullable = false)
    @NotNull
    @Description("Purchasing country")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @ElementCollection
    @CollectionTable(name="order_items", joinColumns=@JoinColumn(name="order_id"))
    @OrderColumn(name = "item_index")
    @BatchSize(size = Constants.BATCH_SIZE)
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    //order result

    @DisplayFormat("na")
    @ManyToOne
    @JoinColumn(name = "shipper_id")
    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    @DisplayFormat("| na")
    @Column(name = "shipped_date")
    @Description("The order is complete when this is populated.")
    public LocalDateTime getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDateTime shippedDate) {
        this.shippedDate = shippedDate;
    }

    @DisplayFormat("na")
    @Column(name = "number_of_parcels")
    @Min(1)
    public Integer getNumberOfParcels() {
        return numberOfParcels;
    }

    public void setNumberOfParcels(Integer numberOfParcels) {
        this.numberOfParcels = numberOfParcels;
    }

    @Watermark("currency value in your locale")
    @DisplayFormat("currency | na")
    @Column(name = "freight", precision = Constants.CURRENCY_PRECISION, scale = Constants.CURRENCY_SCALE)
    @DecimalMin(value = "0.0")
    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    @DisplayFormat("na")
    @Column(name = "ship_name", length = Constants.MID_STRING_LENGTH)
    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    @DisplayFormat("na")
    @Embedded
    public Address getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(Address shipAddress) {
        this.shipAddress = shipAddress;
    }
}
