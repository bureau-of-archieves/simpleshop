package simpleshop.domain.metadata.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;

/**
 * The min validation for a string.
 * Validated value must >= the value specified here.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinStringValidator.class)
@java.lang.annotation.Documented
public @interface MinString {
    String value();

    String message() default "{sponge.validation.validation.min_string}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
