package simpleshop.dto;


import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.domain.metadata.DisplayFormat;
import simpleshop.domain.model.Category;
import simpleshop.domain.model.Supplier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AliasDeclaration(propertyName = "categories", aliasName = "cat")
public class ProductSearch extends ModelSearch {

    private String name;
    private Category category;
    private List<Category> categories;
    private CategorySearch categoryPrefix;
    private Supplier supplier;
    private List<Integer> productIds;
    private BigDecimal minSellPrice;

    @PropertyFilter(operator = PropertyFilter.Operator.LIKE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyFilter(property = "categories", operator = PropertyFilter.Operator.CONTAINS)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @PropertyFilter(property = "categories", operator = PropertyFilter.Operator.CONTAINS_ANY)
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @PropertyFilter(property = "categories", operator = PropertyFilter.Operator.CONTAINS_MATCH)
    public CategorySearch getCategoryPrefix() {
        return categoryPrefix;
    }

    public void setCategoryPrefix(CategorySearch categoryPrefix) {
        this.categoryPrefix = categoryPrefix;
    }

    @PropertyFilter
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @PropertyFilter(property = "id", operator = PropertyFilter.Operator.IN)
    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    @PropertyFilter(property = "sellPrice", negate = true, operator = PropertyFilter.Operator.LESS)
    public BigDecimal getMinSellPrice() {
        return minSellPrice;
    }

    public void setMinSellPrice(BigDecimal minSellPrice) {
        this.minSellPrice = minSellPrice;
    }
}
