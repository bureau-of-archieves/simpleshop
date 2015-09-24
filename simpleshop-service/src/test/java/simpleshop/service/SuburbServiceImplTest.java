package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.SuburbDAO;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.Country;
import simpleshop.domain.model.Suburb;
import simpleshop.dto.SuburbSearch;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SuburbServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private SuburbService suburbService;

    @Autowired
    private SuburbDAO suburbDAO;

    @Before
    public void cleanUp() {
        super.cleanUp(suburbDAO, TestConstants.SUBURB_MARK);
    }

    @Test
    public void createDeleteTest() {

        Country australia = new Country("AUS");
        Suburb suburb = new Suburb();
        suburb.setSuburb(TestConstants.SUBURB_MARK + " Test1");
        suburb.setPostcode("2233");
        suburb.setCountry(australia);
        suburbService.save(suburb);

        flush();

        suburbService.delete(suburb);

    }

    @Test
    public void searchTest() {

        Country[] countries = {new Country("AUS"), new Country("USA")};
        for (int i = 0; i < 5; i++) {
            Suburb suburb = new Suburb();
            suburb.setSuburb(TestConstants.SUBURB_MARK + i);
            suburb.setPostcode("100" + i % 2);
            suburb.setCountry(countries[i % 2]);
            suburbService.save(suburb);
        }

        SuburbSearch suburbSearch = new SuburbSearch();
        suburbSearch.setKeywords(TestConstants.SUBURB_MARK);
        suburbSearch.setPageSize(20);
        List<Suburb> suburbs = suburbService.search(suburbSearch);
        assertThat(suburbs.size(), equalTo(5));

        suburbSearch.setCountry(countries[0]);
        suburbs = suburbService.search(suburbSearch);
        assertThat(suburbs.size(), equalTo(3));

        suburbSearch.setCountry(countries[1]);
        suburbs = suburbService.search(suburbSearch);
        assertThat(suburbs.size(), equalTo(2));

    }
}
