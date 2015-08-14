package simpleshop.dto;


import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.domain.model.Category;

@AliasDeclaration(propertyName = "categories", aliasName = "cat")
public class ProductSearch extends ModelSearch {

    private String name;
    private Category category;

    @PropertyFilter(operator = PropertyFilter.Operator.LIKE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyFilter(alias = "cat", property = "this", operator = PropertyFilter.Operator.CONTAINS)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
