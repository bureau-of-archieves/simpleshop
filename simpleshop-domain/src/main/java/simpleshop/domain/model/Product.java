package simpleshop.domain.model;

import org.hibernate.annotations.BatchSize;
import simpleshop.Constants;
import simpleshop.domain.metadata.Description;
import simpleshop.domain.metadata.Icon;
import simpleshop.domain.metadata.Summary;
import simpleshop.domain.model.component.ProductSupplier;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
@Table(name = "products")
@Icon("shopping-cart")
public class Product {
    private Integer id;
    private String name;
    private String imageUrl;
    private String quantityPerUnit;
    private Set<Category> categories;
    private Set<ProductSupplier> productSuppliers;
    private Integer stock = 0;

    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Summary
    @Column(length = Constants.MID_STRING_LENGTH, nullable = false)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Summary
    @Column(length = Constants.MID_STRING_LENGTH)
    @Description("Main image of the product.")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Summary
    @Column(length = Constants.MID_STRING_LENGTH, nullable = false)
    @NotNull
    public String getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(String quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    @OneToMany
    @JoinTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @BatchSize(size = Constants.BATCH_SIZE)
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @ElementCollection
    @CollectionTable(name="product_suppliers", joinColumns=@JoinColumn(name="product_id"))
    @BatchSize(size = Constants.BATCH_SIZE)
    public Set<ProductSupplier> getProductSuppliers() {
        return productSuppliers;
    }

    public void setProductSuppliers(Set<ProductSupplier> productSuppliers) {
        this.productSuppliers = productSuppliers;
    }

    @Summary
    @Column(name = "stock", nullable = false)
    @NotNull
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}