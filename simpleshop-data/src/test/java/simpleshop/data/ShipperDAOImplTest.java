package simpleshop.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Shipper;
import simpleshop.domain.model.Suburb;
import simpleshop.domain.model.component.Address;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for <code>shipperDAO</code>
 */
public class ShipperDAOImplTest extends TransactionalTest {

    @Autowired
    private ShipperDAO shipperDAO;

    @Autowired
    private SuburbDAO suburbDAO;

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

        shipperDAO.delete(shipper);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Integer createShipper(String name) {
        Shipper shipper = new Shipper();
        shipper.setContact(new Contact());
        shipper.getContact().setName(name);

        shipperDAO.save(shipper);
        return shipper.getId();
    }

}
