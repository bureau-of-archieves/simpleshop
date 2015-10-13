package simpleshop.business;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import simpleshop.data.ProductDAO;

import javax.annotation.PostConstruct;

/**
 * Do validation by invoking Drools rules definedi n this module.
 */
public class BusinessValidator implements Validator {

    KieContainer kieContainer;

    @Autowired
    private ProductDAO productDAO;

    @PostConstruct
    public void init(){
        KieServices kieServices = KieServices.Factory.get();
        kieContainer = kieServices.getKieClasspathContainer();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        if(clazz.getName().contains(".dto.") ||clazz.getName().contains(".domain."))
            return true;
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

        StatelessKieSession session = kieContainer.newStatelessKieSession();
        session.getGlobals().set("productDAO", productDAO);
        session.getGlobals().set("errors", errors);
        session.execute(target);
    }
}
