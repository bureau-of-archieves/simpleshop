package simpleshop.dto;


import simpleshop.data.metadata.PropertyFilter;

/**
 * Search model for a Customer object.
 */
public class CustomerSearch extends ContactSearch {

    private Integer id;

    @PropertyFilter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
