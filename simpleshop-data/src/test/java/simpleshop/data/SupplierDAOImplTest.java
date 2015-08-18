package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Suburb;
import simpleshop.domain.model.Supplier;
import simpleshop.domain.model.component.Address;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for <code>SupplierDAOImpl</code>.
 */
public class SupplierDAOImplTest extends TransactionalTest {

    @Autowired
    private SupplierDAO supplierDAO;

    @Autowired
    private SuburbDAO suburbDAO;

    @Test
    public void createDeleteTest(){

        List<Suburb> suburbs = suburbDAO.quickSearch(TestConstants.SUBURB_AUS_1, new PageInfo());
        assertThat(suburbs.size(), equalTo(1));

        Supplier supplier = new Supplier();
        supplier.setContact(new Contact());
        supplier.getContact().setName(TestConstants.JON_SNOW);
        supplier.getContact().setContactName("999 " + TestConstants.SUPPLIER_MARK);
        supplier.getContact().getContactNumbers().put("Phone", "11111");
        supplier.getContact().getContactNumbers().put("Fax", "222222");
        supplier.setStock(Boolean.TRUE);
        Address address = new Address();
        address.setSuburb(suburbs.get(0));
        address.setAddressLine1("64 bit Address");
        supplier.getContact().setAddress(address);

        supplierDAO.save(supplier);
        supplierDAO.sessionFlush();

        assertNotNull(supplier.getId());

        supplierDAO.evict(supplier);
        supplier = supplierDAO.get(supplier.getId());

        assertNotNull(supplier);
        assertNotNull(supplier.getContact());
        assertEquals(TestConstants.JON_SNOW, supplier.getContact().getName());
        assertEquals(Boolean.TRUE, supplier.getStock());
        assertThat(supplier.getContact().getContactNumbers().size(), equalTo(2));
        assertThat(supplier.getContact().getContactNumbers().get("Phone"), equalTo("11111"));
        assertThat(supplier.getContact().getContactNumbers().get("Fax"), equalTo("222222"));
        assertThat(supplier.getContact().getAddress().getAddressLine1(), equalTo("64 bit Address"));
        assertThat(supplier.getContact().getAddress().getSuburb(), equalTo(suburbs.get(0)));

        supplierDAO.delete(supplier);
    }

    @Test
    public void quickSearchTest(){

        List<Supplier> suppliers = new ArrayList<>();
        for(int i=0; i<10; i++){
            Supplier supplier = new Supplier();
            supplier.getContact().setName(TestConstants.SUPPLIER_MARK + i);
            supplier.getContact().setContactName("MyValue_" + (i % 2 == 0));
            supplierDAO.save(supplier);
            suppliers.add(supplier);
        }
        supplierDAO.sessionFlush();

        List<Supplier> searchResult = supplierDAO.quickSearch(TestConstants.SUPPLIER_MARK, new PageInfo(0, 20));
        assertThat(searchResult.size(), equalTo(10));

        searchResult = supplierDAO.quickSearch(TestConstants.SUPPLIER_MARK, new PageInfo(0, 6));
        assertThat(searchResult.size(), equalTo(6));

        searchResult = supplierDAO.quickSearch(TestConstants.SUPPLIER_MARK, new PageInfo(1, 6));
        assertThat(searchResult.size(), equalTo(4));

        PageInfo pageInfo = new PageInfo(0, 6);
        pageInfo.setPageSizePlusOne(true);

        searchResult = supplierDAO.quickSearch(TestConstants.SUPPLIER_MARK, pageInfo);
        assertThat(searchResult.size(), equalTo(7));

        pageInfo.setPageIndex(1);
        searchResult = supplierDAO.quickSearch(TestConstants.SUPPLIER_MARK, pageInfo);
        assertThat(searchResult.size(), equalTo(4));

        searchResult = supplierDAO.quickSearch("MyValue_true", new PageInfo(0, 10));
        assertThat(searchResult.size(), equalTo(5));

        searchResult = supplierDAO.quickSearch("MyValue_false", new PageInfo(0, 10));
        assertThat(searchResult.size(), equalTo(5));

    }

    @Before
    public void cleanUp() {
        cleanUp(supplierDAO, TestConstants.SUPPLIER_MARK);
    }

}
