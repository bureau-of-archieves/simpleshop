package simpleshop.domain.model;


import simpleshop.Constants;
import simpleshop.domain.metadata.DisplayFormat;
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "suburb", nullable = false, length = Constants.MID_STRING_LENGTH)
    @NotNull
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
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "postcode", nullable = false, length = Constants.SHORT_STRING_LENGTH)
    @NotNull
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_code", nullable = false)
    @NotNull
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Suburb suburb1 = (Suburb) o;

        if (!suburb.equals(suburb1.suburb)) return false;
        if (postcode != null ? !postcode.equals(suburb1.postcode) : suburb1.postcode != null) return false;
        return country.equals(suburb1.country);

    }

    @Override
    public int hashCode() {
        int result = suburb.hashCode();
        result = 31 * result + (postcode != null ? postcode.hashCode() : 0);
        result = 31 * result + country.hashCode();
        return result;
    }
}
