package simpleshop.domain.model;

import simpleshop.Constants;
import simpleshop.domain.metadata.ItemText;
import simpleshop.domain.metadata.ItemValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "countries")
public class Country implements Comparable<Country>{

    private String countryCode;
    private String name;
    private String currencySymbol;

    @Id
    @Column(name = "country_code", length = 3, nullable = false, updatable = false)
    @NotNull
    @ItemValue
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "name", nullable = false, length = Constants.SHORT_STRING_LENGTH)
    @NotNull
    @ItemText
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "currency_symbol", length = 3, nullable = false)
    @NotNull
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /**
     * Lookup class should implement equals and hashCode with the primary key property.
     * @param o the other object.
     * @return true if keys are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        return countryCode.equals(country.countryCode);

    }

    @Override
    public int hashCode() {
        return countryCode.hashCode();
    }

    @Override
    public int compareTo( Country o) { //to work with tree set
        if(o == null)
            return 1;

        return this.getName().compareTo(o.getName());
    }

    /**
     * This will be used in filtering.
     * @return this should return the same result as defined by @ItemText.
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
