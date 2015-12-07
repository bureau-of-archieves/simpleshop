package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * Getters annotated with NullOut will be nulled out before sending to JSON serialization.
 */
@java.lang.annotation.Target(ElementType.METHOD)
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface NullOut {

    /**
     * @return Groups that activates this NullOut.
     */
    String[] groups() default {""};

}
