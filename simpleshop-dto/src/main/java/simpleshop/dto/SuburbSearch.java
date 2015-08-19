package simpleshop.dto;


import simpleshop.data.metadata.PropertyFilter;
import simpleshop.domain.model.Country;

public class SuburbSearch extends ModelSearch {

    private String keywords;
    private Country country;

    @PropertyFilter.List({
            @PropertyFilter(property = "suburb", operator = PropertyFilter.Operator.LIKE),
            @PropertyFilter(property = "postcode", operator = PropertyFilter.Operator.LIKE)
    })
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @PropertyFilter
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
