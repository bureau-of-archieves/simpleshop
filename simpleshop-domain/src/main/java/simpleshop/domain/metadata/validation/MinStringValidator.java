package simpleshop.domain.metadata.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * MinString validator.
 */
public class MinStringValidator implements ConstraintValidator<MinString, Object> {

    private String minValue = null;

    @Override
    public void initialize(MinString constraintAnnotation) {
        minValue = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(minValue == null || value == null)
            return true;

        return value.toString().compareTo(minValue) >= 0;
    }
}
