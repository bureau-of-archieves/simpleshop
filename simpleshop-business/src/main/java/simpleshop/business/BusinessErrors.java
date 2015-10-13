package simpleshop.business;

import org.kie.api.definition.rule.Rule;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of validation errors.
 */
public class BusinessErrors {

    private List<MessageSourceResolvable> errorMessages = new ArrayList<>();

    public void raise(Rule rule, String modelName, String fieldName){
        FieldError fieldError = new FieldError(modelName, fieldName, rule.getName());
        errorMessages.add(fieldError);
    }

    public List<MessageSourceResolvable> getErrorMessages() {
        return errorMessages;
    }
}
