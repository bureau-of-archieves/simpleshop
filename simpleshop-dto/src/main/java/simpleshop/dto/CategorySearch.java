package simpleshop.dto;

import simpleshop.data.metadata.PropertyFilter;

/**
 * Search parameters for Category model.
 */
public class CategorySearch extends ModelSearch {

    private String name;

    @PropertyFilter(operator = PropertyFilter.Operator.LIKE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
