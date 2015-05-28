package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * Water mark text for the field.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Watermark {
    String value();
}
