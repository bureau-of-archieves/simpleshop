package simpleshop.domain.metadata.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * Convenient annotation for a char field that only allows "A", "I".
 */
@AllowedValues(value = {"A", "I"})
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Status {

    String message()default "{sponge.validation.status}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}