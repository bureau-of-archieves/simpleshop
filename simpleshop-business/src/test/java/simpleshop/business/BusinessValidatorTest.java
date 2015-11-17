package simpleshop.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import simpleshop.dto.CartItem;
import simpleshop.dto.ShoppingCart;

import javax.annotation.Resource;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test the business rules.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-simpleshop-business.xml")
public class BusinessValidatorTest {

    @Resource(name = "businessValidator")
    private Validator businessValidator;

    @Test
    public void canLoadValidationProcess(){
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        Collection<String> names = kieContainer.getKieBaseNames();

        assertThat(names.contains("validationKBase"), equalTo(true));

        KieBase validationKBase = kieContainer.getKieBase("validationKBase");
        org.kie.api.definition.process.Process process = validationKBase.getProcess("validationFlow");
        assertThat(process, notNullValue());
    }

    @Test
    public void cartItemShouldHaveValidProductIdTest(){

        ShoppingCart cart = new ShoppingCart();
        cart.getItems().add(new CartItem());
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(cart, "cart");
        businessValidator.validate(cart, errors);
        assertThat(errors.hasErrors(), equalTo(false));

        cart.getItems().get(0).setProductId(-1);
        errors = new BeanPropertyBindingResult(cart, "cart");
        businessValidator.validate(cart, errors);
        assertThat(RuleTestHelper.hasError(errors, "cartItemShouldHaveValidProductId"), equalTo(true));
    }
}
