package simpleshop.domain.metadata.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;

/**
 * Ensures that the property value is a predefined list of values.
 */
@java.lang.annotation.Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedValuesValidator.class)
@java.lang.annotation.Documented
public @interface AllowedValues {

    String message() default "{sponge.validation.allowed_values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

}
