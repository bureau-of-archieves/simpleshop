package simpleshop.dto;

import simpleshop.data.metadata.PropertyFilter;
import simpleshop.data.metadata.SortProperty;
import simpleshop.domain.model.Category;

/**
 * Search parameters for Category model.
 */
public class CategorySearch extends ModelSearch {

    private String name;
    private Category parentCategory;
    private String prefix;

    @PropertyFilter.List({
            @PropertyFilter(property = "name", operator = PropertyFilter.Operator.LIKE),
            @PropertyFilter(property = "description", operator = PropertyFilter.Operator.LIKE)
    })
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyFilter(property = "parent", operator = PropertyFilter.Operator.EQUAL)
    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @PropertyFilter(operator = PropertyFilter.Operator.START_WITH)
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
