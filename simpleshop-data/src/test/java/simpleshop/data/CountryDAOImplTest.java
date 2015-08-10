package simpleshop.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.CountryDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Country;

import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class CountryDAOImplTest extends TransactionalTest {

    @Autowired
    private CountryDAO CountryDAO;


    @Test
    public void quickSearchTest(){
        List<Country> countries = CountryDAO.quickSearch("CHN", new PageInfo());

        assertThat(countries.size(), greaterThanOrEqualTo(1));

        for(Country Country : countries){
            assertThat(Country.getName(), startsWith("China"));
        }

    }


}
