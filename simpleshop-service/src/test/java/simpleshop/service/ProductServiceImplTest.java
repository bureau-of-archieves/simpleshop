package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.PageInfo;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.Category;
import simpleshop.domain.model.Product;
import simpleshop.domain.model.Supplier;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.CategorySearch;
import simpleshop.dto.ProductSearch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProductServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @Before
    public void cleanUp() {
        super.cleanUp(productService, TestConstants.PRODUCT_MARK);
    }

    @Autowired
    private CategoryService categoryService;

    @Test
    public void createDeleteTest() {
        List<Category> categories = categoryService.quickSearch(TestConstants.SUB_CATEGORY_1, new PageInfo());
        assertThat(categories.size(), equalTo(1));

        List<Supplier> suppliers = supplierService.quickSearch(TestConstants.SUPPLIER_NAME_1, new PageInfo());
        assertThat(suppliers.size(), equalTo(1));

        Product product = productService.create();
        product.setName(TestConstants.PRODUCT_MARK + " - P1");
        product.getCategories().add(categories.get(0));
        product.setQuantityPerUnit("6pcs");
        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setSupplier(suppliers.get(0));
        productSupplier.setUnitPrice(new BigDecimal("25.00"));
        product.getProductSuppliers().add(productSupplier);

        productService.save(product);

        flush();

        assertThat(product.getId(), notNullValue());
    }

    @Test
    public void searchTest() {

        List<Product> products = new ArrayList<>();
        for(int i=0; i<10; i++){
            Product product = new Product();
            product.setName(TestConstants.PRODUCT_MARK + " P1");
            product.setQuantityPerUnit("100g");
            productService.save(product);
            products.add(product);
        }

        flush();

        ProductSearch productSearch = new ProductSearch();
        productSearch.setPageSize(100);
        List<Product> result = productService.search(productSearch);
        assertThat(result.size(), greaterThanOrEqualTo(products.size()));

        productSearch.setName(TestConstants.PRODUCT_MARK);
        result = productService.search(productSearch);
        assertThat(result.size(), greaterThanOrEqualTo(10));

        List<Category> categories = categoryService.quickSearch(TestConstants.SUB_CATEGORY_1, new PageInfo());
        assertThat(categories.size(), equalTo(1));

        for(int i=0; i<4; i++){
            products.get(i).getCategories().add(categories.get(0));
            productService.save(products.get(i));
        }
        flush();

        productSearch.setCategory(categories.get(0));
        result = productService.search(productSearch);
        assertThat(result.size(), greaterThanOrEqualTo(4));

        //test contains any
        List<Category> categories2 = categoryService.quickSearch(TestConstants.SUB_CATEGORY_2, new PageInfo());
        assertThat(categories2.size(), equalTo(1));
        for(int i=4; i<9; i++){
            products.get(i).getCategories().addAll(categories2);
            productService.save(products.get(i));
        }
        flush();
        categories.add(categories2.get(0));

        productSearch.setCategory(null);
        productSearch.setCategories(categories);
        result = productService.search(productSearch);
        assertThat(result.size(), greaterThanOrEqualTo(9));

        productSearch.setPageSize(6);
        result = productService.search(productSearch);
        assertThat(result.size(), greaterThanOrEqualTo(6));

        //test contains match
        productSearch.setPageSize(20);
        productSearch.setCategories(null);
        productSearch.setCategoryPrefix(new CategorySearch());
        List<Category> rootCategory = categoryService.quickSearch(TestConstants.ROOT_CATEGORY, new PageInfo());
        productSearch.getCategoryPrefix().setPrefix(rootCategory.get(0).getPrefix());
        result = productService.search(productSearch);
        assertThat(result.size(), greaterThanOrEqualTo(9));

    }
}
