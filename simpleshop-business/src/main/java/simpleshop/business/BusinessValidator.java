package simpleshop.business;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Do validation by invoking Drools rules definedi n this module.
 */
public class BusinessValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
