package simpleshop.business;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class BusinessValidatorTest {

    @Test
    public void canLoadValidationKBase(){
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        Collection<String> names = kieContainer.getKieBaseNames();

        assertThat(names.contains("validationKBase"), equalTo(true));
    }
}
