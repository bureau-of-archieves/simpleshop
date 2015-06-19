package simpleshop.domain.model.component;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import simpleshop.Constants;
import simpleshop.domain.model.Suburb;


import javax.persistence.*;

@Embeddable
public class Address {

    private String addressLine1;
    private String addressLine2;
    private Suburb suburb;

    @Column(name = "address_line1", length = Constants.LONG_STRING_LENGTH)
    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @Column(name = "address_line2", length = Constants.LONG_STRING_LENGTH)
    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @ManyToOne
    @JoinColumn(name = "suburb_id")
    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }
}
