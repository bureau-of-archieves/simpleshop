package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.ProductSupplier;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ProductDAOImplTest extends TransactionalTest {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private SupplierDAO supplierDAO;

    @Test
    public void getTest(){
        assertThat(productDAO.get(Integer.MAX_VALUE), nullValue());
    }

    private void createProduct(String name, Category category){
        Product product = new Product();
        product.setName(name);
        product.setQuantityPerUnit("500g");
        product.getCategories().add(category);
        productDAO.save(product);
    }

    @Test
    public void createDeleteTest(){

        Product product = new Product();
        product.setName(TestConstants.PRODUCT_MARK + "aa");
        product.setQuantityPerUnit("350g");
        List<Category> categories = categoryDAO.quickSearch("", new PageInfo());
        assertThat(categories.size(), greaterThanOrEqualTo(2));
        product.getCategories().addAll(categories);
        product.setImageUrl("prod1.png");

        productDAO.save(product);
        productDAO.sessionFlush();
        productDAO.evict(product);

        Product loaded = productDAO.load(product.getId());
        assertThat(loaded, not(nullValue()));
        assertThat(loaded.getName(), equalTo(product.getName()));
        assertThat(loaded.getImageUrl(), equalTo(product.getImageUrl()));
        assertThat(loaded.getQuantityPerUnit(), equalTo(product.getQuantityPerUnit()));
        assertThat(loaded.getCategories().size(), equalTo(categories.size()));

        Supplier supplier = supplierDAO.quickSearch("", new PageInfo()).get(0);
        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setSupplier(supplier);
        productSupplier.setUnitPrice(new BigDecimal("12.50"));
        loaded.getProductSuppliers().add(productSupplier);

        productDAO.sessionFlush();
    }

    @Test
    public void quickSearchTest(){

        Category category = categoryDAO.quickSearch(TestConstants.SUB_CATEGORY_1, new PageInfo()).get(0);
        String[] prefix = {" Potato Chips ", " Rice Cracker "};
        for(int i=0; i<10; i++){
            createProduct(TestConstants.PRODUCT_MARK + i + prefix[i % prefix.length], category);
        }

       List<Product> products = productDAO.quickSearch("Cracker", new PageInfo(1,3));
        assertThat(products.size(), equalTo(2));
        for(Product product : products){
            assertThat(product.getName(), containsString("Cracker"));
        }

        products = productDAO.quickSearch(TestConstants.PRODUCT_MARK, new PageInfo(0,Integer.MAX_VALUE));
        assertThat(products.size(), equalTo(10));
    }

    @Before
    public void cleanUp() {
        cleanUp(productDAO, TestConstants.PRODUCT_MARK);
    }


}
