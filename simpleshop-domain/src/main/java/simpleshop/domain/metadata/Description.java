package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * Description text of this field, e.g. a tooltip explaining the meaning of this field.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Description {
    String value();
}
