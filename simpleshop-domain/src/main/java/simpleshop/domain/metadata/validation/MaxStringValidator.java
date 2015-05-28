package simpleshop.domain.metadata.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 30/10/14
 * Time: 10:42 AM
 */
public class MaxStringValidator implements ConstraintValidator<MaxString, Object> {

    private String maxValue = null;

    @Override
    public void initialize(MaxString constraintAnnotation) {
        maxValue = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if(maxValue == null || value == null)
            return true;

        return value.toString().compareTo(maxValue) <= 0;
    }
}
