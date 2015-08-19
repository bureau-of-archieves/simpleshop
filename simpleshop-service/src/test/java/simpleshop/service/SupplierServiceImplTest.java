package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.PageInfo;
import simpleshop.data.SuburbDAO;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.Supplier;
import simpleshop.domain.model.Suburb;
import simpleshop.domain.model.component.Address;
import simpleshop.dto.SupplierSearch;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class SupplierServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private SupplierService supplierService;

    @Before
    public void cleanUp(){
        super.cleanUp(supplierService, TestConstants.CUSTOMER_MARK);
    }

    @Autowired
    private SuburbDAO suburbDAO;

    private List<Supplier> createTestSuppliers(List<Suburb> suburbs){

        String[] contactTypes = {"QQ", "Ph", "Fax", "Email"};
        String[] contactNumbers = {"11112222", "22223333", "33334444", "abc11114444@web.net"};

        List<Supplier> suppliers = new ArrayList<>();
        for(int i=0; i<10; i++){
            Supplier supplier = new Supplier();
            supplier.getContact().setName("Supplier " + (i % 2 == 0));
            supplier.getContact().setContactName(TestConstants.CUSTOMER_MARK + i % 5);
            Address address = new Address();
            address.setSuburb(suburbs.get(i % suburbs.size()));
            address.setAddressLine1(i + (i % 2 == 0 ? "John St." : "Catherine St."));
            supplier.getContact().setAddress(address);

            for(int j=0; j<4; j++){
                supplier.getContact().getContactNumbers().put(contactTypes[j], contactNumbers[j] + "_" + i);
            }
            supplierService.save(supplier);
            suppliers.add(supplier);
        }
        return suppliers;
    }

    @Test
    public void searchTest() {

        List<Suburb> suburbs = suburbDAO.quickSearch("", new PageInfo(0, 10));
        assertThat(suburbs.size(), greaterThanOrEqualTo(3));
        suburbs = suburbs.subList(0,3);
        List<Supplier> testSuppliers = createTestSuppliers(suburbs);
        flush();

        //the default search contains all test suppliers
        SupplierSearch supplierSearch = new SupplierSearch();
        supplierSearch.setPageSize(100);
        List<Supplier> result = supplierService.search(supplierSearch);
        assertThat(result.size(), greaterThanOrEqualTo(testSuppliers.size()));
        for(Supplier testSupplier : testSuppliers){
            assertThat(result.contains(testSupplier), equalTo(true));
        }

        supplierSearch.setAddress("0John St.");
        result = supplierService.search(supplierSearch);
        assertThat(result.size(), equalTo(1));

        testSuppliers.get(0).setStock(Boolean.TRUE);
        supplierService.save(testSuppliers.get(0));
        flush();
        supplierSearch.setStock(true);
        result = supplierService.search(supplierSearch);
        assertThat(result.size(), equalTo(1));

    }

    @Test
    public void createDeleteTest(){

        Supplier supplier = supplierService.create();
        supplier.getContact().setName("bbb" + TestConstants.CUSTOMER_MARK);
        supplierService.save(supplier);

        assertThat(supplier.getId(), notNullValue());
        supplierService.delete(supplier);
        flush();

        supplier = supplierService.getById(supplier.getId());
        assertThat(supplier, nullValue());
    }

}
