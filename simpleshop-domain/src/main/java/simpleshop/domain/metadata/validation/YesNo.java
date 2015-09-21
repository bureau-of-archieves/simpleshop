package simpleshop.domain.metadata.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Convenient annotation for a char field that only allows "Y", "N".
 */
@AllowedValues({"Y", "N"})
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface YesNo {

    String message() default "{sponge.validation.yesno}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
