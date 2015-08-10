package simpleshop.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.*;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ProductDAOImplTest extends TransactionalTest {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void createProduct(String name, Category category){
        Product product = new Product();
        product.setName(name);
        product.setQuantityPerUnit("500g");
        product.getCategories().add(category);
        productDAO.save(product);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void deleteProducts(List<Product> result){
        result.forEach(productDAO::delete);
    }


    @Test
    public void quickSearchTest(){

        Category category = categoryDAO.quickSearch("Food", new PageInfo()).get(0);
        String[] prefix = {"Potato Chips ", "Rice Cracker "};
        for(int i=0; i<10; i++){
            createProduct("quickSearch - " + prefix[i%prefix.length] + (i+1), category);
        }

       List<Product> products = productDAO.quickSearch("Cracker", new PageInfo(1,3));
        assertThat(products.size(), equalTo(2));
        for(Product product : products){
            assertThat(product.getName(), containsString("Cracker"));
        }

        products = productDAO.quickSearch("quickSearch", new PageInfo(0,Integer.MAX_VALUE));
        assertThat(products.size(), greaterThanOrEqualTo(10));
        deleteProducts(products);

    }




}
