package simpleshop.domain.metadata.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;

/**
 * Ensures that the property value is a predefined list of values.
 * User: JOHNZ
 * Date: 3/10/14
 * Time: 5:14 PM
 */
@java.lang.annotation.Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedValuesValidator.class)
@java.lang.annotation.Documented
public @interface AllowedValues {

    String message() default "{simpleshop.domain.metadata.validation.allowed_values}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

}
