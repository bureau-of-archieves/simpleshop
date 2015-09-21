package simpleshop.domain.metadata.validation;

import simpleshop.common.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Implementation for @CompareStringProperties.
 */
public class CompareStringPropertiesValidator implements ConstraintValidator<CompareStringProperties, Object> {

    private String leftPropertyPath;
    private String rightPropertyPath;
    private boolean negate;
    private StringComparison comparisonMethod;

    @Override
    public void initialize(CompareStringProperties constraintAnnotation) {
        this.leftPropertyPath = constraintAnnotation.leftProperty();
        this.rightPropertyPath = constraintAnnotation.rightProperty();
        this.negate = constraintAnnotation.negate();
        this.comparisonMethod = constraintAnnotation.comparisonMethod();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if(value == null)
            return true;

        Object leftValue = ReflectionUtils.getProperty(value, leftPropertyPath);
        if(leftValue == null)
            return true;

        Object rightValue = ReflectionUtils.getProperty(value, rightPropertyPath);
        if(rightValue == null)
            return true;

        String left = leftValue.toString();
        String right = rightValue.toString();

        boolean result = true;
        switch (comparisonMethod) {
            case START_WITH:
                result = left.startsWith(right);
                break;
            case END_WITH:
                result = left.endsWith(right);
                break;
            case CONTAINS:
                result = left.contains(right);
                break;
            case EQUALS:
                result = left.compareTo(right) == 0;
                break;
        }
        if(negate)
            result = !result;

        return result;
    }
}
