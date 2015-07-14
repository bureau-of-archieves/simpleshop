package simpleshop.domain.model;


import simpleshop.Constants;
import simpleshop.domain.metadata.DisplayFormat;
import simpleshop.domain.metadata.ItemText;
import simpleshop.domain.metadata.ItemValue;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "suburbs")
@DisplayFormat("concat:'suburb':'postcode'")
public class Suburb {

    private Integer id;
    private String suburb;
    private String city;
    private String state;
    private String postcode;
    private Country country;

    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    @ItemValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "suburb", nullable = false, length = Constants.MID_STRING_LENGTH)
    @NotNull
    @ItemText
    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    @Column(name = "city", length = Constants.MID_STRING_LENGTH)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "state", length = Constants.MID_STRING_LENGTH)
    @ItemText(order = 1, separator = ", ")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "postcode", nullable = false, length = Constants.SHORT_STRING_LENGTH)
    @NotNull
    @ItemText(order = 2, separator = " ")
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_code", nullable = false)
    @NotNull
    @ItemText(order = 3, separator = " ")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
