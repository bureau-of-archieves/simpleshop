package simpleshop.domain.metadata.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;

/**
 * The Max validation for string.
 * User: JOHNZ
 * Date: 30/10/14
 * Time: 10:27 AM
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxStringValidator.class)
@java.lang.annotation.Documented
public @interface MaxString {

    String value();

    String message() default "{quickshop.domain.metadata.validation.max_string}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
