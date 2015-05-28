package simpleshop.domain.metadata.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

/**
 * The bean validator that validates against th @AllowedValues annotation.
 */
public class AllowedValuesValidator implements ConstraintValidator<AllowedValues, Object>{

    private static final List<Class<?>> allowedTypes = Arrays.asList(new Class<?>[]{String.class,Character.class,  Integer.class, Long.class});
    private List<String> allowedValues;

    @Override
    public void initialize(AllowedValues constraintAnnotation) {
        allowedValues = Arrays.asList(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null)
            return true;

        if(allowedTypes.contains(value.getClass())){
           return allowedValues.contains(value.toString());
        }

        throw new IllegalArgumentException("@AllowedValues cannot be applied to a getter with this return type: " + value.getClass().getName());
    }
}
