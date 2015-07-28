package simpleshop.dto;


import simpleshop.data.metadata.PropertyFilter;

public class OrderSearch extends ModelSearch {

    private String shipName;

    @PropertyFilter(operator = PropertyFilter.Operator.LIKE)
    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
}
