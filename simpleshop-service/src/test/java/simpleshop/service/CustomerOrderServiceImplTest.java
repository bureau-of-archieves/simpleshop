package simpleshop.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.domain.model.Product;
import simpleshop.dto.CartItem;
import simpleshop.dto.CustomerOrder;
import simpleshop.dto.ProductSearch;
import simpleshop.dto.ShoppingCart;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test CustomerOrderServiceImpl.
 */
public class CustomerOrderServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private ProductService productService;

    @Test
    public void canGetCartProducts(){

        List<Product> products = productService.search(new ProductSearch());
        assertThat(products.size(), greaterThan(0));

        ShoppingCart cart = new ShoppingCart();
        for(Product product : products){
            cart.getItems().add(new CartItem(product.getId()));
        }

        Map<Integer, Product> cartProducts = customerOrderService.getCartProducts(cart);

        assertThat(cartProducts.size(), equalTo(products.size()));
        for(Product product : products){
            assertThat(cartProducts, hasKey(product.getId()));
        }
    }

    @Test
    public void createTest(){

        List<Product> products = productService.search(new ProductSearch());
        assertThat(products.size(), greaterThan(0));

        ShoppingCart cart = new ShoppingCart();
        for(Product product : products){
            cart.getItems().add(new CartItem(product.getId()));
        }

        CustomerOrder customerOrder = customerOrderService.create("zhy2002", cart);

        assertThat(customerOrder.getCustomer(), notNullValue());
        assertThat(customerOrder.getOrder().getCustomer(), nullValue());
        assertThat(customerOrder.getOrder().getOrderItems(), hasSize(products.size()));
    }
}
