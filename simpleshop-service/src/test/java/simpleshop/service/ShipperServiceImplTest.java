package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.PageInfo;
import simpleshop.data.ShipperDAO;
import simpleshop.data.SuburbDAO;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.ContactNumberType;
import simpleshop.domain.model.Shipper;
import simpleshop.domain.model.Suburb;
import simpleshop.domain.model.component.Address;
import simpleshop.dto.ShipperSearch;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test <code>MetadataServiceImpl</code> methods.
 */
public class ShipperServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private ShipperService shipperService;

    @Autowired
    private SuburbDAO suburbDAO;

    @Autowired
    private ShipperDAO shipperDAO;

    @Before
    public void cleanUp(){
        super.cleanUp(shipperDAO, TestConstants.SHIPPER_MARK);
    }

    @Test
    public void searchTest()  {

        //create test objects
        List<Suburb> suburbs = suburbDAO.quickSearch("", new PageInfo());
        createShipper(TestConstants.SHIPPER_NAME_1, ContactNumberType.WORK_PHONE.name(), TestConstants.WORK_PHONE_NUMBER_1, TestConstants.MAGIC_STREET_1, suburbs.get(0));
        createShipper(TestConstants.SHIPPER_NAME_2, ContactNumberType.WORK_PHONE.name(), TestConstants.WORK_PHONE_NUMBER_2, TestConstants.CONTACT_STREET_1, suburbs.get(1));

        for (int i = 0; i < 10; i++) {
            createShipper(TestConstants.RAMBO + i, ContactNumberType.WORK_PHONE.name(), i % 2 == 1 ? TestConstants.HOME_PHONE_NUMBER_1 : TestConstants.HOME_PHONE_NUMBER_2, TestConstants.CONTACT_STREET_2, suburbs.get(1));
        }
        flush(); //have to flush here as auto flushing mode does not check the exists sub-query in our search.

        ShipperSearch shipperSearch = new ShipperSearch();
        shipperSearch.setName(TestConstants.SHIPPER_NAME_1);
        List<Shipper> shippers = shipperService.search(shipperSearch);
        assertThat(shippers.size(), equalTo(1));

        shipperSearch.setName(TestConstants.RAMBO);
        shipperSearch.setPageSize(5);
        shippers = shipperService.search(shipperSearch);
        assertThat(shippers.size(), equalTo(5));

        shipperSearch.setContactNumber(TestConstants.HOME_PHONE_NUMBER_2);
        shipperSearch.setPageSize(10);
        shippers = shipperService.search(shipperSearch);
        assertThat(shippers.size(), equalTo(5));
    }

    private Shipper createShipper(String name, String contactKey, String contactNumber, String addressLine, Suburb suburb) {
        Shipper shipper = shipperService.create();
        shipper.getContact().setName(name);
        shipper.getContact().setContactName(TestConstants.SHIPPER_MARK);
        shipper.getContact().getContactNumbers().put(contactKey, contactNumber);

        Address address = new Address();
        address.setAddressLine1(addressLine);
        address.setSuburb(suburb);
        shipper.getContact().setAddress(address);

        shipperService.save(shipper);
        return shipper;
    }
}
