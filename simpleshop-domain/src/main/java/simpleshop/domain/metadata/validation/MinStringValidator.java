package simpleshop.domain.metadata.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 30/10/14
 * Time: 10:42 AM
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
