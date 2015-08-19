package simpleshop.dto;


import simpleshop.data.metadata.PropertyFilter;

/**
 * Search model for a Customer object.
 */
public class CustomerSearch extends ContactSearch {

    private boolean stock = false;

    @PropertyFilter
    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }
}
