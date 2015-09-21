package simpleshop.domain.metadata.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;

/**
 * Raise an error if the string comparison of two (nested) properties of the validated object fails.
 */
@java.lang.annotation.Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompareStringPropertiesValidator.class)
@java.lang.annotation.Documented
public @interface CompareStringProperties {

    String message() default "{sponge.validation.compare_string_properties}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return property of decorated object that goes to the left of the comparison operator.
     */
    String leftProperty();

    boolean negate() default false;

    StringComparison comparisonMethod() default StringComparison.EQUALS;

    /**
     * @return property of decorated object that goes to the right of the comparison operator.
     */
    String rightProperty();
}
