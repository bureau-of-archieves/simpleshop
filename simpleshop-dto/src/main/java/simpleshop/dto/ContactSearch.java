package simpleshop.dto;


import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.data.metadata.SortProperty;
import simpleshop.domain.model.Suburb;


@AliasDeclaration(propertyName = "contact", aliasName = "ct")
@SortProperty.List({
        @SortProperty(alias = "ct", propertyName = "name"),
        @SortProperty(alias = "ct", propertyName = "contactName")
})
public class ContactSearch extends ModelSearch {

    private String name;
    private String contactNumber;
    private String address;
    private Suburb suburb;

    @PropertyFilter.List({
            @PropertyFilter(alias = "ct", property = "name", operator = PropertyFilter.Operator.LIKE),
            @PropertyFilter(alias = "ct", property = "contactName", operator = PropertyFilter.Operator.LIKE)
    })
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyFilter(alias = "ct", property = "contactNumbers.elements", operator = PropertyFilter.Operator.LIKE)
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @PropertyFilter.List({
            @PropertyFilter(alias = "ct", property = "address.addressLine1", operator = PropertyFilter.Operator.LIKE),
            @PropertyFilter(alias = "ct", property = "address.addressLine2", operator = PropertyFilter.Operator.LIKE)
    })
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @PropertyFilter(alias = "ct", property = "address.suburb")
    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }
}
