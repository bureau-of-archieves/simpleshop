package simpleshop.business;

import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import simpleshop.data.ProductDAO;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Do validation of business objects by invoking Drools rules defined in this module.
 */
public class BusinessValidator implements Validator {

    private KieContainer kieContainer;

    @Autowired
    private ProductDAO productDAO;

    @PostConstruct
    public void init(){
        KieServices kieServices = KieServices.Factory.get();
        kieContainer = kieServices.getKieClasspathContainer();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        if(clazz.getName().contains(".dto.") || clazz.getName().contains(".domain."))
            return true;
        return false;
    }

    @Transactional
    @Override
    public void validate(Object target, Errors errors) {

        StatelessKieSession session = kieContainer.newStatelessKieSession();
        List<Command> commands = new ArrayList<>();
        commands.add(CommandFactory.newSetGlobal("productDAO", productDAO));
        commands.add(CommandFactory.newSetGlobal("errors", errors));
        if(target instanceof Iterable){
            @SuppressWarnings("unchecked")
            Iterable<Object> iterable = (Iterable<Object>)target;
            iterable.forEach(item -> commands.add(CommandFactory.newInsert(item)));
        } else {
            commands.add(CommandFactory.newInsert(target));
        }
        commands.add(CommandFactory.newStartProcess("validationFlow"));
        session.execute(CommandFactory.newBatchExecution(commands)); //ignore returned ExecutionResults
    }
}
