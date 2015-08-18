package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Country;
import simpleshop.domain.model.Suburb;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for <code>suburbDAOImpl</code>.
 */
public class SuburbDAOImplTest extends TransactionalTest {

    @Autowired
    private SuburbDAO suburbDAO;

    @Autowired
    private CountryDAO countryDAO;

    @Test
    public void loadTest() {
        List<Suburb> suburbs = suburbDAO.quickSearch("", new PageInfo());
        assertTrue(suburbs.size() > 0);

        Integer suburbId = suburbs.get(0).getId();
        Suburb suburb = suburbDAO.load(suburbId);
        assertNotNull(suburb);
        assertNotNull(suburb.getCountry());
    }

    @Test
    public void createDeleteTest() {
        Suburb suburb = new Suburb();
        suburb.setSuburb(TestConstants.SUBURB_MARK + "aa");
        suburb.setCity("NA");
        suburb.setState("NSW");
        suburb.setPostcode("2212");
        suburb.setCountry(new Country("AUS"));
        suburbDAO.save(suburb);
        assertNotNull(suburb.getId());

        suburbDAO.evict(suburb);

        Suburb loaded = suburbDAO.load(suburb.getId());
        assertThat(loaded, not(sameInstance(suburb)));

        assertThat(loaded.hashCode(), equalTo(suburb.hashCode()));
        assertThat(loaded, equalTo(suburb));

        suburbDAO.delete(loaded);
    }

    @Test
    public void quickSearchTest() {

        List<Country> countries = countryDAO.quickSearch("", new PageInfo(0, 3));
        assertThat(countries.size(), equalTo(3));

        for(int i=0; i< 10; i++){
            Suburb suburb = new Suburb();
            suburb.setSuburb(TestConstants.SUBURB_MARK + i);
            suburb.setCountry(countries.get(i % 3));
            suburb.setPostcode("PC" + i % 2);
            suburbDAO.save(suburb);
        }
        suburbDAO.sessionFlush();

        List<Suburb> result = suburbDAO.quickSearch(TestConstants.SUBURB_MARK, new PageInfo(0, 20));
        assertThat(result.size(), equalTo(10));

        result = suburbDAO.quickSearch("PC0", new PageInfo(0, 20));
        assertThat(result.size(), equalTo(5));

        result = suburbDAO.quickSearch("PC1", new PageInfo(0, 20));
        assertThat(result.size(), equalTo(5));

        PageInfo pageInfo = new PageInfo(0, 5);
        pageInfo.setPageSizePlusOne(true);
        result = suburbDAO.quickSearch(TestConstants.SUBURB_MARK, pageInfo);
        assertThat(result.size(), equalTo(6));

        pageInfo.setPageIndex(1);
        result = suburbDAO.quickSearch(TestConstants.SUBURB_MARK, pageInfo);
        assertThat(result.size(), equalTo(5));

        pageInfo.setPageIndex(2);
        result = suburbDAO.quickSearch(TestConstants.SUBURB_MARK, pageInfo);
        assertThat(result.size(), equalTo(0));
    }

    @Before
    public void cleanUp() {
        cleanUp(suburbDAO, TestConstants.SUBURB_MARK);
    }

}
