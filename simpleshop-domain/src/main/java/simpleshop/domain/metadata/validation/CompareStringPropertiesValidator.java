package simpleshop.domain.metadata.validation;

import simpleshop.common.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

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
        Object rightValue = ReflectionUtils.getProperty(value, rightPropertyPath);
        String left = leftValue == null ? null : leftValue.toString();
        String right = rightValue == null ? null : rightValue.toString();

        boolean result = true;
        switch (comparisonMethod) {
            case START_WITH:
                result = left == null || right == null || left.startsWith(right);
                break;
            case END_WITH:
                result = left == null || right == null || left.endsWith(right);
                break;
            case CONTAINS:
                result = left == null || right == null || left.contains(right);
                break;
            case EQUALS:
                result = Objects.equals(left, right);
                break;
        }
        if(negate)
            result = !result;

        return result;
    }
}
