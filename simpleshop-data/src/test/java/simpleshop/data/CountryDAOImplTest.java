package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Country;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CountryDAOImplTest extends TransactionalTest {

    @Autowired
    private CountryDAO countryDAO;

    @Test
    public void getTest(){
        assertThat(countryDAO.get("ZZ9"), nullValue());
    }

    @Test
    public void quickSearchTest(){
        List<Country> countries = countryDAO.quickSearch("CHN", new PageInfo());

        assertThat(countries.size(), equalTo(1));

        for(Country Country : countries){
            assertThat(Country.getName(), startsWith("China"));
        }

        countries = countryDAO.quickSearch("XXX", new PageInfo());
        assertThat(countries.size(), equalTo(0));
    }

    @Test
    public void createDeleteTest(){

        Country country = new Country(TestConstants.COUNTRY_MARK + "1");
        country.setName("My Test");
        country.setCurrencySymbol("#");
        countryDAO.save(country);
        countryDAO.sessionFlush();
        countryDAO.evict(country);
        int hash = country.hashCode();
        int idHash = System.identityHashCode(country);

        country = countryDAO.get(TestConstants.COUNTRY_MARK + "1");
        assertThat(country.hashCode(), equalTo(hash));
        assertThat(System.identityHashCode(country), not(equalTo(idHash)));
        assertThat(country, notNullValue());
        assertThat(country.getCurrencySymbol(), equalTo("#"));

        countryDAO.delete(country);
        countryDAO.sessionFlush();
    }

    @Before
    public void cleanUp() {
        cleanUp(countryDAO, TestConstants.COUNTRY_MARK);
    }


}
