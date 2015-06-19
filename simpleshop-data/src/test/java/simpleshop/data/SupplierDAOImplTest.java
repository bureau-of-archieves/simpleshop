package simpleshop.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Supplier;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for <code>SupplierDAOImpl</code>.
 */
public class SupplierDAOImplTest extends TransactionalTest {

    @Autowired
    private SupplierDAO supplierDAO;

    @Test
    public void createDeleteTest(){
        Supplier supplier = new Supplier();
        supplier.setContact(new Contact());
        supplier.getContact().setName(TestConstants.JON_SNOW);
        supplier.setStock(Boolean.TRUE);

        supplierDAO.save(supplier);

        assertNotNull(supplier.getId());

        supplierDAO.evict(supplier);

        supplier = supplierDAO.get(supplier.getId());
        assertNotNull(supplier);
        assertNotNull(supplier.getContact());
        assertEquals(TestConstants.JON_SNOW, supplier.getContact().getName());
        assertEquals(Boolean.TRUE, supplier.getStock());

        List<Supplier> suppliers = supplierDAO.quickSearch(TestConstants.JON_SNOW, new PageInfo());
        assertTrue(suppliers.size() == 1);

        supplierDAO.delete(supplier);
    }
}
