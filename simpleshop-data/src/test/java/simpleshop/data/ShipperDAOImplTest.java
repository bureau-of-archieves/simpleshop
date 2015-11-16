package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Shipper;
import simpleshop.domain.model.Suburb;
import simpleshop.domain.model.component.Address;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for <code>shipperDAO</code>
 */
public class ShipperDAOImplTest extends TransactionalTest {

    @Autowired
    private ShipperDAO shipperDAO;

    @Autowired
    private SuburbDAO suburbDAO;

    @Test
    public void getTest(){
        assertThat(shipperDAO.get(Integer.MAX_VALUE), nullValue());
    }

    @Test
    public void quickSearchTest() {
        for (int i = 0; i < 10; i++) {
            Shipper shipper = new Shipper();
            shipper.getContact().setName(i + TestConstants.SHIPPER_MARK);
            shipper.getContact().setContactName(i % 2 == 0 ? "Mr Zhang" : "Mrs Zhang");
            shipperDAO.save(shipper);
        }
        shipperDAO.sessionFlush();

        List<Shipper> shippers = shipperDAO.quickSearch("ZZZ57859098543", new PageInfo());
        assertThat(shippers.size(), equalTo(0));

        shippers = shipperDAO.quickSearch(TestConstants.SHIPPER_MARK, new PageInfo(0,20));
        assertThat(shippers.size(), equalTo(10));

        shippers = shipperDAO.quickSearch(TestConstants.SHIPPER_MARK, new PageInfo(0,9));
        assertThat(shippers.size(), equalTo(9));

        shippers = shipperDAO.quickSearch(1 + TestConstants.SHIPPER_MARK, new PageInfo(0,9));
        assertThat(shippers.size(), equalTo(1));
    }

    @Test
    public void createDeleteTest() {
        Integer shipperId = createShipper(TestConstants.TEST_MAN_1_NAME);
        Shipper shipper = shipperDAO.load(shipperId);
        assertEquals(TestConstants.TEST_MAN_1_NAME, shipper.getContact().getName());

        List<Suburb> suburbs = suburbDAO.quickSearch("Wollongong", new PageInfo());
        assertTrue(suburbs.size() > 0);
        Address address = new Address();
        address.setAddressLine1(TestConstants.MAGIC_STREET_1);
        address.setSuburb(suburbs.get(0));
        shipper.getContact().setAddress(address);
        shipperDAO.save(shipper);
        shipperDAO.sessionFlush();
        shipperDAO.evict(shipper);

        shipper = shipperDAO.get(shipperId);
        assertNotNull(shipper.getContact().getAddress());
        assertEquals(TestConstants.MAGIC_STREET_1, shipper.getContact().getAddress().getAddressLine1());
        assertNotNull(shipper.getContact().getAddress().getSuburb());
        assertEquals("Wollongong", shipper.getContact().getAddress().getSuburb().getSuburb());

        Contact contact = shipper.getContact();
        shipper.getContact().getContactNumbers().put("QQ", "32131232154");
        shipper.getContact().getContactNumbers().put("email", "abc@web.net");
        shipperDAO.sessionFlush();
        shipperDAO.evict(shipper);

        shipper = shipperDAO.get(shipperId);
        assertThat(shipper.getContact().getContactNumbers().size(), equalTo(2));
        assertThat(shipper.getContact().getContactNumbers().get("QQ"), equalTo("32131232154"));
        assertThat(shipper.getContact().getContactNumbers().get("email"), equalTo("abc@web.net"));

        shipperDAO.delete(shipper);
    }

    private Integer createShipper(String name) {
        Shipper shipper = new Shipper();
        shipper.getContact().setContactName(TestConstants.SHIPPER_MARK);
        shipper.getContact().setName(name);

        shipperDAO.save(shipper);
        return shipper.getId();
    }

    @Before
    public void cleanUp() {
        cleanUp(shipperDAO, TestConstants.SHIPPER_MARK);
    }


}
