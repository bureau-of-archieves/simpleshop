package simpleshop.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Country;
import simpleshop.domain.model.Suburb;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for <code>suburbDAOImpl</code>.
 */
public class SuburbDAOImplTest extends TransactionalTest {

    @Autowired
    private SuburbDAO suburbDAO;

    @Test
    public void loadTest(){
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
        suburb.setSuburb(TestConstants.SUBURB_AUS_1);
        suburb.setPostcode(TestConstants.SUBURB_AUS_1_POSTCODE);
        suburb.setCountry(new Country("AUS"));
        suburbDAO.save(suburb);
        assertNotNull(suburb.getId());

        suburbDAO.delete(suburb);
    }



}
