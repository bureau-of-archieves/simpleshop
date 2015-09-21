package simpleshop.domain.metadata.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;

/**
 * The Max validation for string.
 * The validated value must be <= the value specified here.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxStringValidator.class)
@java.lang.annotation.Documented
public @interface MaxString {

    String value();

    String message() default "{sponge.validation.max_string}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
